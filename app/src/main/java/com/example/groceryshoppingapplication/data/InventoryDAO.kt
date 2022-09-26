package com.example.groceryshoppingapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.groceryshoppingapplication.enums.ProductAvailability
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.models.demoUser


@Dao
interface InventoryDAO {
    @Insert
    suspend fun addNewItem(groceryItemEntity: GroceryItemEntity)

    @Update
    suspend fun restockItem(groceryItemEntity: GroceryItemEntity)

    @Query("select COUNT(productCode) from Inventory where productCode = :productCode")
    suspend fun getItemQuantity(productCode: Int):Int

    @Query("select * from Inventory where productCode = :productCode")
    suspend fun getItemDetails(productCode: Int): GroceryItemEntity

    @Query("select productAvailability from Inventory where productCode = :productCode")
    suspend fun checkStockAvailability(productCode: Int): ProductAvailability

    @Query("select * from Inventory")
    fun getAllItemsData(): LiveData<List<GroceryItemEntity>>

    @Query("SELECT * FROM user")
     fun getAllUsers(): List<demoUser>
}