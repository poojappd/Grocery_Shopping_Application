package com.example.groceryshoppingapplication.viewmodels

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.system.Os.remove
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

class ModifyOrderViewModel(application: Context) :ViewModel() {

    lateinit var orderedItems: List<OrderedItemEntity>
    lateinit var orderDetail: OrderDetail
    val inventoryDao = AppDatabase.getDatabase(application).getInventoryDao()
    val modifiableOrderItems = MutableLiveData<MutableList<OrderedItemEntity>>()
    var newPrice = MutableLiveData<Double>()
    var newQty =  MutableLiveData<Int>()
    var modifiedSessionEnabled = false

    fun setOrderDetails(orderedItems: List<OrderedItemEntity>, orderDetail: OrderDetail){
        this.orderedItems = orderedItems
        this.orderDetail = orderDetail
        modifiedSessionEnabled = true
        modifiableOrderItems.value = orderedItems.toMutableList()
        newPrice.value = orderDetail.subTotal
        newQty.value =orderDetail.numberOfItems

    }

    fun haltModifyingOrder(){
        modifiedSessionEnabled = false
    }

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


    fun addToModifiableOrders(orderedItemEntity: OrderedItemEntity){
        modifiableOrderItems.value =
            modifiableOrderItems.value!!.toMutableList().apply {
                add(orderedItemEntity)
            }
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
            modifiableOrderItems.value =modifiableOrderItems.value!!.toMutableList().apply {
                remove(currentOrderedItem)
            }

            modifiableOrderItems.value = modifiableOrderItems.value!!.toMutableList().apply {
                add(position, updatedOrderedItem)
            }
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

    fun getCurrentOrderItemQuantity(productCode:Int):LiveData<Int>?{
        for (i in modifiableOrderItems.value!!){
            if(i.productCode == productCode)
                return MutableLiveData
        }
    }

}

class ModifyOrderViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return ModifyOrderViewModel(context) as T
    }
}




