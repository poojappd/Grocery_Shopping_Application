package com.example.groceryshoppingapplication.repositories

import androidx.lifecycle.LiveData
import com.example.groceryshoppingapplication.data.InventoryDAO
import com.example.groceryshoppingapplication.models.GroceryItemEntity

class InventoryRepository(private val inventoryDAO: InventoryDAO) {
    val productsInInventory: LiveData<List<GroceryItemEntity>> = inventoryDAO.getAllItemsData()

    fun getProduct(productCode:Int):LiveData<GroceryItemEntity> = inventoryDAO.getItemDetails(productCode)
}