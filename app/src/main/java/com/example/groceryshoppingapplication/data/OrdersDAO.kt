package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.relations.OrderDetailAndOrderedItems
import com.example.groceryshoppingapplication.relations.UserAndOrders

@Dao
interface OrdersDAO {

    @Insert
    suspend fun createOrder(order: OrderDetail)

    @Insert
    suspend fun addOrderedItem(orderItem:OrderedItemEntity)

    @Query("update OrderDetail set orderStatus = :orderStatus where orderId = :orderId")
    suspend fun updateOrderStatus(orderStatus: OrderStatus, orderId: String)

    @Query("select * from OrderDetail where orderId = :orderId")
    fun getOrderDetail(orderId: String):OrderDetail

    @Update
    suspend fun updateOrderDetail(order: OrderDetail)

    @Update
    suspend fun updateOrderedItemEntity(orderItem: OrderedItemEntity)

    @Transaction
    @Query("select * from User where userId = :userId")
    fun getUserOrders(userId:String): UserAndOrders

    @Transaction
    @Query("select * from OrderDetail where orderId = :orderId")
    fun getOrderedItemsFromOrder(orderId:String): OrderDetailAndOrderedItems


}