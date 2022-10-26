package com.example.groceryshoppingapplication.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.repositories.OrdersRepository
import kotlinx.coroutines.launch

class OrderHistoryViewModel(applicationContext: Context) : ViewModel() {
    private val _allOrders = MutableLiveData<List<OrderDetail>>()
    private val myOrdersRepo = OrdersRepository(AppDatabase.getDatabase(applicationContext))
    private lateinit var userId: String


    val allOrders: LiveData<List<OrderDetail>>
        get() = _allOrders

    fun refreshMyOrders(userId:String) {
        val orders=  myOrdersRepo.getUserOrders(userId)
        _allOrders.value = myOrdersRepo.getUserOrders(userId).ordersInfo
    }

    fun getOrderItemsFromOrder(orderId: String): List<OrderedItemEntity> {
        return myOrdersRepo.getOrderedItemsFromOrder(orderId).orderedItems
    }

    fun getOrderDetail(orderId: String)= myOrdersRepo.getOrderDetail(orderId)

    fun createNewOrder(order: OrderDetail, orderedItems:List<OrderedItemEntity>) =
        viewModelScope.launch {
            myOrdersRepo.createOrder(order)
            orderedItems.forEach {
                addOrderedItemToOrder(it)
            }
            refreshMyOrders(order.userId)
        }

    private fun addOrderedItemToOrder(orderItem: OrderedItemEntity) = viewModelScope.launch {
        myOrdersRepo.addOrderedItemToOrder(orderItem)
    }

    fun updateOrderStatus(orderStatus: OrderStatus, orderId: String) = viewModelScope.launch {
        myOrdersRepo.updateOrderStatus(orderStatus, orderId)
    }
}

class OrderHistoryViewModelFactory(
    private val applicationContext: Context,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return OrderHistoryViewModel(
            applicationContext
        ) as T
    }
}