package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
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
//    val modifyOrderViewModel: ModifyOrderViewModel by viewModels {
//        ModifyOrderViewModel.ModifyOrderViewModelFactory(
//            orderHistoryViewModel.getOrderItemsFromOrder(args.orderId),
//            orderHistoryViewModel.getOrderDetail(args.orderId).value!!
//        )
//    }
    lateinit var totalPriceTv: TextView
    val modifiableOrderItems = MutableLiveData<MutableList<OrderedItemEntity>>()
    lateinit var modifiableOrderDetail :OrderDetail
    var newPrice = MutableLiveData<Double>()
    var newQty = MutableLiveData<Int>()
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

        modifiableOrderDetail= orderHistoryViewModel.getOrderDetail(args.orderId).value!!
        modifiableOrderItems.value = orderHistoryViewModel.getOrderItemsFromOrder(args.orderId).toMutableList()
        orderDetail= orderHistoryViewModel.getOrderDetail(args.orderId).value!!
        orderedItems = orderHistoryViewModel.getOrderItemsFromOrder(args.orderId)
        newQty.value = orderDetail.numberOfItems
        newPrice.value=orderDetail.subTotal
        val modifyItemListener = ModifyItemListenerImpl()
        modifiableOrderItems.observe(viewLifecycleOwner) {
            Log.e(TAG, "size in observe ${it.size}")
            recyclerView.adapter = ModifyOrderedItemsAdapter(it,modifyItemListener)
        }
        newPrice.observe(viewLifecycleOwner) {
            view.totalPrice_modifyOrder_fragment.text = modifyItemListener.decimal.format(it)
        }
        view.saveChanges_modifyOrder.setOnClickListener {
            modifyItemListener.saveChanges()
        }

        return view
    }

    inner class ModifyItemListenerImpl() : ModifyItemListener {
        private val toastMessageProvider = ToastMessageProvider(requireContext())
        val totalProductSize = modifiableOrderItems.value!!.size
        val lifecycleOwner = viewLifecycleOwner
        override val decimal = DecimalFormat("0.##")

        fun getOrderItem(position: Int): MutableLiveData<OrderedItemEntity> {
            return MutableLiveData(modifiableOrderItems.value!!.get(position))
        }

        override fun removeFromOrder(
            orderedItemEntity: OrderedItemEntity,
            removeCompletely: Boolean
        ): Double {
            modifiableOrderItems.value = modifiableOrderItems.value!!.toMutableList().apply {
                remove(orderedItemEntity)
            }
            if (removeCompletely) {
                newQty.value = newQty.value!!.dec()
                newPrice.value = newPrice.value!!.minus(
                    orderedItemEntity.quantity * inventoryViewModel.getProductDetailsSynchronously(
                        orderedItemEntity.productCode
                    ).unitPrice
                )
            }

            Log.e(TAG,"REMOVED ${orderedItemEntity.productCode}")
            return newPrice.value!!
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
            val orderedItemEntity = modifiableOrderItems.value!!.get(position)
            val currentQty = orderedItemEntity.quantity
            val updatedOrderedItem: OrderedItemEntity
            if (currentQty > 1) {
                updatedOrderedItem = OrderedItemEntity(
                    orderedItemEntity.orderId,
                    orderedItemEntity.productCode,
                    currentQty - 1,
                    orderedItemEntity.id
                )
                removeFromOrder(orderedItemEntity)

                modifiableOrderItems.value = modifiableOrderItems.value!!.toMutableList().apply {
                    add(position,updatedOrderedItem)
                }
                newPrice.value = newPrice.value!!.minus(
                    inventoryViewModel.getProductDetailsSynchronously(orderedItemEntity.productCode).unitPrice
                )
                Log.e(TAG, "Decreased qty -"+updatedOrderedItem.quantity.toString())

                return newPrice.value!!
            } else {
                if (modifiableOrderItems.value!!.size > 1) {
                    return removeFromOrder(orderedItemEntity, true)
                }
                //cancel option
            }
            return (-1).toDouble()
        }

        private fun getOriginalOrderItem(orderItemId:String): OrderedItemEntity? {
            orderedItems.forEach {
                if(it.id == orderItemId)
                    return it
            }
            return null
        }
        override fun increaseQuantity(position: Int): Double {
            val orderedItemEntity = modifiableOrderItems.value!!.get(position)
            val currentQty = orderedItemEntity.quantity
            val initialQty = getOriginalOrderItem(orderedItemEntity.id)!!.quantity
            val inventoryItem = inventoryViewModel.getProductDetailsSynchronously(orderedItemEntity.productCode)
            val countInInventory = inventoryItem.availableQuantity


                val updatedOrderedItem: OrderedItemEntity
            if (currentQty < 5) {
                if (currentQty<initialQty || countInInventory>1) {
                    updatedOrderedItem = OrderedItemEntity(
                        orderedItemEntity.orderId,
                        orderedItemEntity.productCode,
                        currentQty + 1,
                        orderedItemEntity.id
                    )
                    removeFromOrder(orderedItemEntity)
                    modifiableOrderItems.value =
                        modifiableOrderItems.value!!.toMutableList().apply {
                            add(position, updatedOrderedItem)
                        }
                    newPrice.value = newPrice.value!!.plus(
                        inventoryItem.unitPrice
                    )
                    Log.e(TAG, "Increased qty -" + updatedOrderedItem.quantity.toString())

                    return newPrice.value!!
                }
                else
                    toastMessageProvider.show("No more items left")

            } else
                toastMessageProvider.show("Maximum ordering quantity is 5")


                return (-1).toDouble()
            }



        override fun saveChanges() {
            val formatedPrice = decimal.format(newPrice.value!!).toDouble()
            if (!modifiableOrderItems.value!!.equals(orderedItems)) {
                val updatedOrder = OrderDetail(
                    orderDetail.orderId,
                    orderDetail.userId,
                    formatedPrice,
                    orderDetail.orderDate,
                    newQty.value!!,
                    orderDetail.deliverySlot,
                    orderDetail.deliveryAddress,
                    orderDetail.mobileNumber,
                    15.toDouble(),
                    formatedPrice + 15,
                    orderDetail.paymentMethod,
                )
                Log.e(TAG, updatedOrder.toString())
                orderHistoryViewModel.updateOrderChanges(modifiableOrderItems.value!!, updatedOrder)

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