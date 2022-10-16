package com.example.groceryshoppingapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.ProductAvailability
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.relations.CartItemAndProduct


@Dao
interface InventoryDAO {
    @Insert
    suspend fun addNewItem(groceryItemEntity: GroceryItemEntity)

    @Update
    suspend fun restockItem(groceryItemEntity: GroceryItemEntity)

    @Query("select COUNT(productCode) from Inventory where productCode = :productCode")
    suspend fun getItemQuantity(productCode: Int):Int

    @Query("select * from Inventory where subCategory = :subCategory")
    fun getProductsUnderSubCategory(subCategory: SubCategory): List<GroceryItemEntity>

    @Query("select * from Inventory where category = :category")
    fun getProductsUnderGeneralCategory(category: GeneralCategory):List<GroceryItemEntity>

    @Query("select * from Inventory where productCode = :productCode")
    fun getItemDetails(productCode: Int): LiveData<GroceryItemEntity>

    @Query("select * from Inventory where productCode = :productCode")
    fun getItemDetailsSynchronously(productCode: Int): GroceryItemEntity

    @Query("select productAvailability from Inventory where productCode = :productCode")
    suspend fun checkStockAvailability(productCode: Int): ProductAvailability

    @Query("select * from Inventory")
    fun getAllItemsData(): LiveData<List<GroceryItemEntity>>

    @Transaction
    @Query("select * from Inventory where productCode=:productCode")
    suspend fun fetchCartItemDetails(productCode: Int): CartItemAndProduct

}

