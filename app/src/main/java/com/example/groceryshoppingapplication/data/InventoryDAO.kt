package com.example.groceryshoppingapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.groceryshoppingapplication.GroceryItem
import com.example.groceryshoppingapplication.enums.ProductAvailability

@Dao
interface InventoryDAO {
    @Insert
    suspend fun addNewItem(groceryItem: GroceryItem)

    @Update
    suspend fun restockItem(groceryItem: GroceryItem)

    @Query("select COUNT(productCode) from Inventory where productCode = :productCode")
    suspend fun getItemQuantity(productCode: Int)

    @Query("select * from Inventory where productCode = :productCode")
    suspend fun getItemDetails(productCode: Int): GroceryItem

    @Query("select availability from Inventory where productCode = :productCode")
    suspend fun checkStockAvailability(productCode: Int): ProductAvailability

}