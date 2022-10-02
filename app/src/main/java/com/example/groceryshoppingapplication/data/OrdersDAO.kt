package com.example.groceryshoppingapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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

    @Transaction
    @Query("select * from OrderDetail where userId = :userId")
    fun getUserOrders(userId:Int): List<UserAndOrders>

    @Transaction
    @Query("select * from OrderDetail where orderId = :orderId")
    fun getOrderedItemsFromOrder(orderId:String): List<OrderDetailAndOrderedItems>


}