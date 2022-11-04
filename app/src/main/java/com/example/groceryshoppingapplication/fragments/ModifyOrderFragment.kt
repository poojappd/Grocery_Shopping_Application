package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.nfc.Tag
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.ToastMessageProvider
import com.example.groceryshoppingapplication.adapters.ModifyOrderedItemsAdapter
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.listeners.ModifyItemListener
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_modify_order.view.*
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*
import kotlinx.android.synthetic.main.product_not_available_dialog_layout.view.*
import kotlinx.android.synthetic.main.product_not_available_dialog_layout.view.button
import java.text.DecimalFormat
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*

class ModifyOrderFragment : Fragment() {

    val orderHistoryViewModel: OrderHistoryViewModel by viewModels {
        OrderHistoryViewModelFactory(requireActivity().applicationContext)
    }
    val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    val modifyOrderViewModel: ModifyOrderViewModel by activityViewModels {
        ModifyOrderViewModelFactory(requireContext().applicationContext)
    }
    val deliverySlotViewModel:DeliverySlotViewModel by activityViewModels {
        DeliverySlotViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modify_order, container, false)
        val date = SimpleDateFormat("dd MMM yyyy - hh:mma").parse(modifyOrderViewModel.orderDetail.orderDate)
        val oneHourAfterOrderedDate = Calendar.getInstance()
        oneHourAfterOrderedDate.time = date
        oneHourAfterOrderedDate.add(Calendar.HOUR, 1)
        if (oneHourAfterOrderedDate.after(Date())) {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val dialogView =
                layoutInflater.inflate(R.layout.product_not_available_dialog_layout, null)

            dialogBuilder.setView(dialogView)
            val dialogMessage = dialogView.dialog_message_product
            val yesButtonDialog = dialogView.button
            val alertDialog = dialogBuilder.create()
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow()!!
                    .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
            }
            dialogView.productRecyclerVIew.visibility = View.GONE

            dialogMessage.text = StringBuilder("Sorry!\n The time limit for modifying thi order is out")
            yesButtonDialog.setOnClickListener { view ->
                alertDialog.cancel()
                modifyOrderViewModel.haltModifyingOrder()
                findNavController().popBackStack()
            }

                alertDialog.show()

            orderHistoryViewModel.updateOrderStatus(
                OrderStatus.CONFIRMED,
                modifyOrderViewModel.orderDetail.orderId,
            )
        }
        view.toolbar_ModifyOrder.setNavigationOnClickListener { requireActivity().onBackPressed() }
        val recyclerView = view.modifyOrder_recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val modifyItemListener = ModifyItemListenerImpl()
        modifyOrderViewModel.modifiableOrderItems.observe(viewLifecycleOwner) {
            Log.e(TAG, "size in observe ${it.size}")
            recyclerView.adapter = ModifyOrderedItemsAdapter(it, modifyItemListener)
        }
        modifyOrderViewModel.newPrice.observe(viewLifecycleOwner) {
            view.totalPrice_modifyOrder_fragment.text = modifyItemListener.decimal.format(it)
        }
        view.saveChanges_modifyOrder.setOnClickListener {
            modifyItemListener.saveChanges()
        }
        view.exit_modifyOrder.setOnClickListener {
            modifyOrderViewModel.haltModifyingOrder()
            findNavController().popBackStack()
        }

        return view
    }

    inner class ModifyItemListenerImpl() : ModifyItemListener {
        private val toastMessageProvider = ToastMessageProvider(requireContext())
        val lifecycleOwner = viewLifecycleOwner
        override val decimal = DecimalFormat("0.##")

        override fun removeFromOrder(orderedItemEntity: OrderedItemEntity) {
            modifyOrderViewModel.removeFromOrder(orderedItemEntity)
        }

        override fun getOrderedItemExtras(productCode: Int): ModifyOrderItemData {
            val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
            val appendString =
                if (product.capacity > 1 && product.capacityUnit.value.length > 2) "s" else ""
            return ModifyOrderItemData(
                product.brandName + " " + product.itemName + " " + product.capacity + " " + product.capacityUnit.value + appendString,
                product.unitPrice,
                product.capacity,
                product.capacityUnit.value
            )
        }

        override fun getContext(): Context = requireContext()

        override fun decreaseQuantity(position: Int) {
            modifyOrderViewModel.decreaseQuantity(position)
        }

        override fun increaseQuantity(position: Int) {
            val orderedItemEntity = modifyOrderViewModel.modifiableOrderItems.value!!.get(position)
            val currentQty = orderedItemEntity.quantity
            val initialQty =
                modifyOrderViewModel.getOriginalOrderItemQuantity(orderedItemEntity.productCode)?:1
            val inventoryItem =
                inventoryViewModel.getProductDetailsSynchronously(orderedItemEntity.productCode)
            val countInInventory = inventoryItem.availableQuantity

            if (currentQty < 5) {
                if (currentQty < initialQty || countInInventory > 1) {
                    modifyOrderViewModel.increaseQuantity(position)
                } else
                    toastMessageProvider.show("No more items left")
            } else
                toastMessageProvider.show("Maximum ordering quantity is 5")
        }



        override fun saveChanges(){
            if (!modifyOrderViewModel.modifiableOrderItems.value!!.equals(modifyOrderViewModel.orderedItems)) {
                val formatedPrice = decimal.format(modifyOrderViewModel.newPrice.value!!).toDouble()
            val updatedOrder = OrderDetail(
                modifyOrderViewModel.orderDetail.orderId,
                modifyOrderViewModel.orderDetail.userId,
                formatedPrice,
                modifyOrderViewModel.orderDetail.orderDate,
                modifyOrderViewModel.modifiableOrderItems.value!!.size,
                modifyOrderViewModel.orderDetail.deliverySlot,
                modifyOrderViewModel.orderDetail.deliveryAddress,
                modifyOrderViewModel.orderDetail.mobileNumber,
                15.toDouble(),
                formatedPrice + 15,
                modifyOrderViewModel.orderDetail.paymentMethod,
            )
            modifyOrderViewModel.modifiedOrderDetail = updatedOrder
            findNavController().navigate(R.id.deliverySlotFragment)
            }
         else {
            toastMessageProvider.show("No changes found")
                modifyOrderViewModel.haltModifyingOrder()
                val action = ModifyOrderFragmentDirections.actionModifyOrderFragmentToOrderSummaryFragment(modifyOrderViewModel.orderDetail.orderId)
                findNavController().navigate(action)
            }

        }
    }


    data class ModifyOrderItemData(
        val productTitle: String,
        val productPrice: Double,
        val quantity: Double,
        val quantityUnit: String
    )
}