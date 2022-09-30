package com.example.groceryshoppingapplication.repositories

import androidx.room.Insert
import androidx.room.Query
import com.example.groceryshoppingapplication.data.OrdersDAO
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

class OrdersRepository(private val ordersDAO: OrdersDAO) {


    suspend fun createOrder(order: OrderDetail) = ordersDAO.createOrder(order)

    suspend fun addOrderedItem(orderItem: OrderedItemEntity) = ordersDAO.addOrderedItem(orderItem)

    suspend fun updateOrderStatus(orderStatus: OrderStatus, orderId: String) =
        ordersDAO.updateOrderStatus(orderStatus, orderId)

    fun getUserOrders(userId: Int) = ordersDAO.getUserOrders(userId)

    fun getOrderedItemsFromOrder(orderId: String) = ordersDAO.getOrderedItemsFromOrder(orderId)
}