package com.example.groceryshoppingapplication.repositories

import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.data.OrdersDAO
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

class OrdersRepository(database: AppDatabase) {
    private val ordersDAO: OrdersDAO = database.getOrdersDao()

    suspend fun createOrder(order: OrderDetail) = ordersDAO.createOrder(order)

    suspend fun addOrderedItemToOrder(orderItem: OrderedItemEntity) = ordersDAO.addOrderedItem(orderItem)

    suspend fun updateOrderStatus(orderStatus: OrderStatus, orderId: String) =
        ordersDAO.updateOrderStatus(orderStatus, orderId)

    fun getUserOrders(userId: String) = ordersDAO.getUserOrders(userId)

    fun getOrderedItemsFromOrder(orderId: String) = ordersDAO.getOrderedItemsFromOrder(orderId)
}