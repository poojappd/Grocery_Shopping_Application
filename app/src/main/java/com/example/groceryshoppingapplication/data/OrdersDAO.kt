package com.example.groceryshoppingapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.ModifiedOrderEntity
import com.example.groceryshoppingapplication.models.ModifiedOrderItemEntity
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

    @Query("delete from OrderedItemEntity where orderId = :orderId")
    suspend fun removeOrderedItemsFromOrder(orderId:String)

    @Update
    suspend fun updateOrderDetail(order: OrderDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrderedItemEntity(orderItem: OrderedItemEntity)

    @Transaction
    @Query("select * from User where userId = :userId")
    fun getUserOrders(userId:String): UserAndOrders

    @Transaction
    @Query("select * from OrderDetail where orderId = :orderId")
    fun getOrderedItemsFromOrder(orderId:String): OrderDetailAndOrderedItems

    @Query("select quantity from ModifiedOrderItemEntity where  orderId= :orderId and productCode = :productCode")
    fun getModifiedOrderItemQuantity(productCode: Int, orderId: String): LiveData<Int>?

    @Insert
    suspend fun addModifiedOrderItem(modifiedOrderItemEntity: ModifiedOrderItemEntity)

    @Query("update ModifiedOrderItemEntity set quantity = quantity + 1 where productCode = :productCode and orderId=:orderId")
    suspend fun increaseQuantity(productCode: Int, orderId: String)

    @Query("update ModifiedOrderItemEntity set quantity = quantity - 1 where productCode = :productCode and orderId=:orderId")
    suspend fun decreaseQuantity(productCode: Int, orderId: String)

    @Query("delete from ModifiedOrderItemEntity where orderId = :orderId and productCode = :productCode")
    suspend fun removeFromModifiedOrderItems(orderId: String,productCode: Int)

    @Query("select * from ModifiedOrderItemEntity where orderId=:orderId")
    suspend fun getModifiedOrderItems(orderId: String):List<ModifiedOrderItemEntity>

    @Query("delete from ModifiedOrderItemEntity")
    suspend fun clearTableModifiedItems()

    @Query("delete from ModifiedOrderEntity")
    suspend fun clearTableModifiedOrder()

}