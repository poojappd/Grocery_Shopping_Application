package com.example.groceryshoppingapplication.viewmodels

import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.repositories.OrdersRepository
import kotlinx.coroutines.launch

class OrderHistoryViewModel(applicationContext: Context, private val userId: String) : ViewModel() {
    private val _allOrders = MutableLiveData<List<OrderDetail>>()
    private val myOrdersRepo = OrdersRepository(AppDatabase.getDatabase(applicationContext))



    val allOrders: LiveData<List<OrderDetail>>
        get() = _allOrders

    fun refreshMyOrders() {
        _allOrders.value = myOrdersRepo.getUserOrders(userId).ordersInfo
    }

    fun getOrderItemsFromOrder(orderId: String): List<OrderedItemEntity> {
        return myOrdersRepo.getOrderedItemsFromOrder(orderId).orderedItems
    }

    fun createNewOrder(order: OrderDetail) =
        viewModelScope.launch {
            myOrdersRepo.createOrder(order)
            refreshMyOrders()
        }

    fun addOrderedItemToOrder(orderItem: OrderedItemEntity) = viewModelScope.launch {
        myOrdersRepo.addOrderedItemToOrder(orderItem)
    }

    fun updateOrderStatus(orderStatus: OrderStatus, orderId: String) = viewModelScope.launch {
        myOrdersRepo.updateOrderStatus(orderStatus, orderId)
    }
}

class OrderHistoryViewModelFactory(
    private val applicationContext: Context,
    private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return OrderHistoryViewModel(
            applicationContext, userId
        ) as T
    }
}