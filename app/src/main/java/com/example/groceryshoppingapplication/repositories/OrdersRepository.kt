package com.example.groceryshoppingapplication.repositories

import androidx.room.Update
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

    suspend fun removeOrderedItemsFromOrder(orderId:String) = ordersDAO.removeOrderedItemsFromOrder(orderId)

    fun getUserOrders(userId: String) = ordersDAO.getUserOrders(userId)

    fun getOrderDetail(orderId: String):OrderDetail = ordersDAO.getOrderDetail(orderId)

    fun getOrderedItemsFromOrder(orderId: String) = ordersDAO.getOrderedItemsFromOrder(orderId)

    suspend fun updateOrderDetail(order: OrderDetail) = ordersDAO.updateOrderDetail(order)

    suspend fun updateOrderedItemEntity(orderItem: OrderedItemEntity) = ordersDAO.updateOrderedItemEntity(orderItem)
}