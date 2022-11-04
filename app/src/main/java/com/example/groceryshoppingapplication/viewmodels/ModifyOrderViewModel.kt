package com.example.groceryshoppingapplication.viewmodels

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.system.Os.remove
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.models.ModifiedOrderItemEntity
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import kotlinx.coroutines.launch

class ModifyOrderViewModel(application: Context) : ViewModel() {

    lateinit var orderedItems: List<OrderedItemEntity>
    lateinit var orderDetail: OrderDetail
    val inventoryDao = AppDatabase.getDatabase(application).getInventoryDao()
    val ordersDao = AppDatabase.getDatabase(application).getOrdersDao()
    val modifiableOrderItems = MutableLiveData<MutableList<OrderedItemEntity>>()
    lateinit var modifiedOrderDetail:OrderDetail
    var newPrice = MutableLiveData<Double>()
    var newQty = MutableLiveData<Int>()
    var modifiedSessionEnabled = false

    fun checkOrderDetailIsSet(): Boolean {
        return (this@ModifyOrderViewModel::orderDetail.isInitialized)

        }
    fun setOrderDetails(orderedItems: List<OrderedItemEntity>, orderDetail: OrderDetail) {
        MyGroceryApplication.setModifiedStateEnabled(true, orderDetail.orderId)
        this.orderedItems = orderedItems
        this.orderDetail = orderDetail
        modifiedSessionEnabled = true
        modifiableOrderItems.value = orderedItems.toMutableList()
        newPrice.value = orderDetail.subTotal
        newQty.value = orderDetail.numberOfItems
        for (item in orderedItems){
            addToModifiableOrders(item)
        }

    }

    fun haltModifyingOrder() =viewModelScope.launch{
        modifiedSessionEnabled = false
        ordersDao.clearTableModifiedItems()
        ordersDao.clearTableModifiedOrder()
        MyGroceryApplication.setModifiedStateEnabled(false,"")
    }

    fun removeFromOrder(
        orderedItemEntity: OrderedItemEntity) = viewModelScope.launch {
        ordersDao.removeFromModifiedOrderItems(
            orderId = orderedItemEntity.orderId,
            orderedItemEntity.productCode
        )
        newPrice.value = newPrice.value!!.minus(
            orderedItemEntity.quantity * inventoryDao.getItemDetailsSynchronously(orderedItemEntity.productCode).unitPrice
        )
        refreshModifiedOrderItem()
    }

    private fun refreshModifiedOrderItem() =
        viewModelScope.launch {
            if(this@ModifyOrderViewModel::orderDetail.isInitialized){
                val modifiedItems = mutableListOf<OrderedItemEntity>()
                var totapPrice = 0.0
                ordersDao.getModifiedOrderItems(orderDetail.orderId).forEach {
                    val equivalentOrderedItem =
                        OrderedItemEntity(it.orderId, it.productCode, it.quantity, it.id)
                    modifiedItems.add(equivalentOrderedItem)
                    val inventoryItem =
                        inventoryDao.getItemDetailsSynchronously(it.productCode)
                    totapPrice+=(inventoryItem.unitPrice * it.quantity)
                }
                modifiableOrderItems.value = modifiedItems
                newPrice.value = totapPrice
            }
        }


    fun increaseQuantity(position: Int) {
        viewModelScope.launch {
            val currentOrderedItem = modifiableOrderItems.value!!.get(position)
            val currentQty = currentOrderedItem.quantity
            val initialQty = getOriginalOrderItemQuantity(currentOrderedItem.productCode)?:1
            val inventoryItem =
                inventoryDao.getItemDetailsSynchronously(currentOrderedItem.productCode)
            val countInInventory = inventoryItem.availableQuantity

            if (currentQty < 5) {
                if (currentQty < initialQty || countInInventory > 1) {
                    ordersDao.increaseQuantity(
                        currentOrderedItem.productCode,
                        currentOrderedItem.orderId
                    )
                    newPrice.value = newPrice.value!!.plus(
                        inventoryItem.unitPrice
                    )

                }
                refreshModifiedOrderItem()
            }
        }
    }


    fun addToModifiableOrders(orderedItemEntity: OrderedItemEntity)=viewModelScope.launch {
        ordersDao.addModifiedOrderItem(
            ModifiedOrderItemEntity(orderedItemEntity.productCode, orderedItemEntity.orderId, orderedItemEntity.quantity, orderedItemEntity.id)
        )
        refreshModifiedOrderItem()
    }

    fun decreaseQuantity(position: Int) = viewModelScope.launch {
        val currentOrderedItem = modifiableOrderItems.value!!.get(position)
        val currentQty = currentOrderedItem.quantity
        val updatedOrderedItem: OrderedItemEntity
        if (currentQty > 1) {
            newPrice.value = newPrice.value!!.minus(
                inventoryDao.getItemDetailsSynchronously(currentOrderedItem.productCode).unitPrice
            )
            ordersDao.decreaseQuantity(currentOrderedItem.productCode, currentOrderedItem.orderId)

        } else {
            if (modifiableOrderItems.value!!.size > 1) {
                removeFromOrder(currentOrderedItem)
            }
        }
        refreshModifiedOrderItem()
    }

    fun getOriginalOrderItemQuantity(productCode: Int): Int? {
        orderedItems.forEach {
            if (it.productCode == productCode)
                return it.quantity
        }
        return null
    }

    fun getCurrentOrderItemQuantity(productCode: Int): LiveData<Int>? {
        return ordersDao.getModifiedOrderItemQuantity(productCode, orderDetail.orderId)

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





