package com.example.groceryshoppingapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

class ModifyOrderViewModel(val orderedItems: List<OrderedItemEntity>, val orderDetail: OrderDetail) :ViewModel() {
//
//    val modifiableOrderItems = MutableLiveData<MutableList<OrderedItemEntity>>()
//    lateinit var modifiableOrderDetail : OrderDetail
//    var newPrice = MutableLiveData<Double>()
//    var newQty = MutableLiveData<Int>()
//
//    fun decreaseQuantity(position: Int): Double {
//        val orderedItemEntity = modifiableOrderItems.value!!.get(position)
//        val currentQty = orderedItemEntity.quantity
//        val updatedOrderedItem: OrderedItemEntity
//        if (currentQty > 1) {
//            updatedOrderedItem = OrderedItemEntity(
//                orderedItemEntity.orderId,
//                orderedItemEntity.productCode,
//                currentQty - 1,
//                orderedItemEntity.id
//            )
//            modifiableOrderItems.value!!.remove(orderedItemEntity)
//
//            modifiableOrderItems.value!!.add(position,updatedOrderedItem)
//            newPrice.value = newPrice.value!!.minus(
//                inventoryViewModel.getProductDetailsSynchronously(orderedItemEntity.productCode).unitPrice
//            )
//            Log.e(ContentValues.TAG, "Decreased qty -"+updatedOrderedItem.quantity.toString())
//
//            return newPrice.value!!
//        } else {
//            if (modifiableOrderItems.value!!.size > 1) {
//                return removeFromOrder(orderedItemEntity, true)
//            }
//        }


}

class ModifyOrderViewModelFactory(val orderedItems: List<OrderedItemEntity>, val orderDetail: OrderDetail) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return ModifyOrderViewModel(
            orderedItems, orderDetail
        ) as T
    }
}




