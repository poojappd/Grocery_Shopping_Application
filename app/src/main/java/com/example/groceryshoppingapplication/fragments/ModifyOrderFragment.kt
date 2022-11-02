package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.ToastMessageProvider
import com.example.groceryshoppingapplication.adapters.ModifyOrderedItemsAdapter
import com.example.groceryshoppingapplication.listeners.ModifyItemListener
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_modify_order.view.*
import java.text.DecimalFormat
import java.text.FieldPosition

class ModifyOrderFragment : Fragment() {

    val args: ModifyOrderFragmentArgs by navArgs()
    val orderHistoryViewModel: OrderHistoryViewModel by viewModels {
        OrderHistoryViewModelFactory(requireActivity().applicationContext)
    }
    val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    val modifyOrderViewModel: ModifyOrderViewModel by viewModels {
        ModifyOrderViewModelFactory(
            orderHistoryViewModel.getOrderItemsFromOrder(args.orderId),
            orderHistoryViewModel.getOrderDetail(args.orderId).value!!,
            requireContext()
        )
    }

    lateinit var orderedItems: List<OrderedItemEntity>
    lateinit var orderDetail: OrderDetail


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modify_order, container, false)

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

        return view
    }

    inner class ModifyItemListenerImpl() : ModifyItemListener {
        private val toastMessageProvider = ToastMessageProvider(requireContext())
        val lifecycleOwner = viewLifecycleOwner
        override val decimal = DecimalFormat("0.##")

        override fun removeFromOrder(orderedItemEntity: OrderedItemEntity) {
            modifyOrderViewModel.removeFromOrder(orderedItemEntity, true)
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

        override fun decreaseQuantity(position: Int): Double {
            return modifyOrderViewModel.decreaseQuantity(position)
        }

        override fun increaseQuantity(position: Int): Double {
            val orderedItemEntity = modifyOrderViewModel.modifiableOrderItems.value!!.get(position)
            val currentQty = orderedItemEntity.quantity
            val initialQty =
                modifyOrderViewModel.getOriginalOrderItemQuantity(orderedItemEntity.id)!!
            val inventoryItem =
                inventoryViewModel.getProductDetailsSynchronously(orderedItemEntity.productCode)
            val countInInventory = inventoryItem.availableQuantity

            if (currentQty < 5) {
                if (currentQty < initialQty || countInInventory > 1) {
                    return modifyOrderViewModel.increaseQuantity(position)
                } else
                    toastMessageProvider.show("No more items left")
            } else
                toastMessageProvider.show("Maximum ordering quantity is 5")
            return (-1).toDouble()
        }


        override fun saveChanges() {
            val formatedPrice = decimal.format(modifyOrderViewModel.newPrice.value!!).toDouble()
            if (!modifyOrderViewModel.modifiableOrderItems.value!!.equals(orderedItems)) {
                val updatedOrder = OrderDetail(
                    orderDetail.orderId,
                    orderDetail.userId,
                    formatedPrice,
                    orderDetail.orderDate,
                    modifyOrderViewModel.newQty.value!!,
                    orderDetail.deliverySlot,
                    orderDetail.deliveryAddress,
                    orderDetail.mobileNumber,
                    15.toDouble(),
                    formatedPrice + 15,
                    orderDetail.paymentMethod,
                )
                Log.e(TAG, updatedOrder.toString())
                orderHistoryViewModel.updateOrderChanges(
                    modifyOrderViewModel.modifiableOrderItems.value!!,
                    updatedOrder
                )

            } else {
                toastMessageProvider.show("No changes found")
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