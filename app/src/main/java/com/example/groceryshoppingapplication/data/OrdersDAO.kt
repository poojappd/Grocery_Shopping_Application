package com.example.groceryshoppingapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

@Dao
interface OrdersDAO {

    @Insert
    suspend fun createOrder(order: OrderDetail)

    @Insert
    suspend fun addOrderedItem(orderItem:OrderedItemEntity)

    @Query("update OrderDetail set orderStatus = :orderStatus where orderId = orderId")
    suspend fun updateOrderStatus(orderStatus: OrderStatus, orderId: String)

    @Query("select * from OrderDetail where userId = :userId")
    fun getUserOrders(userId:Int)

    @Query("select * from OrderDetail")
    fun getOrderedItemsFromOrder(orderId:String)


}