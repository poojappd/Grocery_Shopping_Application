package com.example.groceryshoppingapplication.viewmodels

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

class ModifyOrderViewModel(val orderedItems: List<OrderedItemEntity>, val orderDetail: OrderDetail, context: Context) :ViewModel() {
    val inventoryDao = AppDatabase.getDatabase(context).getInventoryDao()

    val modifiableOrderItems = MutableLiveData(orderedItems.toMutableList())
    lateinit var modifiableOrderDetail :OrderDetail
    var newPrice = MutableLiveData<Double>(orderDetail.subTotal)
    var newQty = MutableLiveData<Int>(orderDetail.numberOfItems)



    fun removeFromOrder(
        orderedItemEntity: OrderedItemEntity,
        removeCompletely: Boolean,
    ): Double {
        modifiableOrderItems.value = modifiableOrderItems.value!!.toMutableList().apply {
            remove(orderedItemEntity)
        }
        if (removeCompletely) {
            newQty.value = newQty.value!!.dec()
            newPrice.value = newPrice.value!!.minus(
                orderedItemEntity.quantity * inventoryDao.getItemDetailsSynchronously(orderedItemEntity.productCode).unitPrice
            )
        }

        Log.e(ContentValues.TAG,"REMOVED ${orderedItemEntity.productCode}")
        return newPrice.value!!
    }

    fun increaseQuantity(position: Int): Double {
        val currentOrderedItem = modifiableOrderItems.value!!.get(position)
        val currentQty = currentOrderedItem.quantity
        val initialQty = getOriginalOrderItemQuantity(currentOrderedItem.id)!!
        val inventoryItem = inventoryDao.getItemDetailsSynchronously(currentOrderedItem.productCode)
        val countInInventory = inventoryItem.availableQuantity


        val updatedOrderedItem: OrderedItemEntity
        if (currentQty < 5) {
            if (currentQty<initialQty || countInInventory>1) {
                updatedOrderedItem = OrderedItemEntity(
                    currentOrderedItem.orderId,
                    currentOrderedItem.productCode,
                    currentQty + 1,
                    currentOrderedItem.id
                )
                removeFromOrder(currentOrderedItem,false)
                modifiableOrderItems.value =
                    modifiableOrderItems.value!!.toMutableList().apply {
                        add(position, updatedOrderedItem)
                    }
                newPrice.value = newPrice.value!!.plus(
                    inventoryItem.unitPrice
                )
                Log.e(ContentValues.TAG, "Increased qty -" + updatedOrderedItem.quantity.toString())

                return newPrice.value!!
            }
        }
        return (-1).toDouble()
    }


    fun decreaseQuantity(position: Int): Double {
        val currentOrderedItem = modifiableOrderItems.value!!.get(position)
        val currentQty = currentOrderedItem.quantity
        val updatedOrderedItem: OrderedItemEntity
        if (currentQty > 1) {
            updatedOrderedItem = OrderedItemEntity(
                currentOrderedItem.orderId,
                currentOrderedItem.productCode,
                currentQty - 1,
                currentOrderedItem.id
            )
            modifiableOrderItems.value!!.remove(currentOrderedItem)

            modifiableOrderItems.value!!.add(position,updatedOrderedItem)
            newPrice.value = newPrice.value!!.minus(
                inventoryDao.getItemDetailsSynchronously(currentOrderedItem.productCode).unitPrice
            )
            Log.e(ContentValues.TAG, "Decreased qty -"+updatedOrderedItem.quantity.toString())

            return newPrice.value!!
        } else {
            if (modifiableOrderItems.value!!.size > 1) {
                return removeFromOrder(currentOrderedItem, true)
            }
        }
        return (-1).toDouble()
    }

    fun getOriginalOrderItemQuantity(orderItemId:String): Int? {
        orderedItems.forEach {
            if(it.id == orderItemId)
                return it.quantity
        }
        return null
    }

}

class ModifyOrderViewModelFactory(val orderedItems: List<OrderedItemEntity>, val orderDetail: OrderDetail,private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return ModifyOrderViewModel(
            orderedItems, orderDetail, context
        ) as T
    }
}




