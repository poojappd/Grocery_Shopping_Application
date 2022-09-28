package com.example.groceryshoppingapplication.data

import androidx.room.Dao
import androidx.room.Insert
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity

@Dao
interface OrdersDAO {

    @Insert
    suspend fun createOrder(order: OrderDetail)

    @Insert
    suspend fun addOrderedItem(orderItem:OrderedItemEntity)


}