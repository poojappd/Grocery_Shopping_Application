package com.example.groceryshoppingapplication.repositories

import androidx.lifecycle.LiveData
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.data.InventoryDAO
import com.example.groceryshoppingapplication.enums.ProductAvailability
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.GroceryItemEntity

class InventoryRepository(appDatabase: AppDatabase) {
    private val inventoryDAO: InventoryDAO = appDatabase.getInventoryDao()
    val productsInInventory: LiveData<List<GroceryItemEntity>> = inventoryDAO.getAllItemsData()

    fun getProductDetails(productCode: Int): LiveData<GroceryItemEntity> =
        inventoryDAO.getItemDetails(productCode)

    private suspend fun updateInventory(groceryItemEntity: GroceryItemEntity) =
        inventoryDAO.restockItem(groceryItemEntity)

    fun getProductDetailsSynchronously(productCode: Int): GroceryItemEntity =
        inventoryDAO.getItemDetailsSynchronously(productCode)
    //to be called at checkout phase

    suspend fun fetchProductFromInventory(productCode: Int, quantity: Int): Response {
        val product = getProductDetails(productCode)
        product.value?.let {
            val availableQuantity = it.availableQuantity
            if (availableQuantity >= quantity) {
                val remainingProductQuantity = availableQuantity - quantity
                val productAvailability =
                    if (remainingProductQuantity > 0) ProductAvailability.IN_STOCK else ProductAvailability.OUT_OF_STOCK

                val updatedProduct = it.copy(
                    availableQuantity = remainingProductQuantity,
                    productAvailability = productAvailability
                )
                updateInventory(updatedProduct)
                return Response.ITEMS_REMOVED_FROM_INVENTORY

            }
            return Response.INSUFFICIENT_QUANTITY_IN_INVENTORY
        }
        return Response.NO_SUCH_ITEM_IN_INVENTORY
    }

    suspend fun restoreProductToInventory(productCode: Int, quantity: Int): Response {
        if (quantity > 0) {
            val product = getProductDetails(productCode)
            product.value?.let {
                val availableQuantity = it.availableQuantity
                val updatedQuantity = availableQuantity + quantity
                val updatedProduct = it.copy(
                    availableQuantity = updatedQuantity,
                    productAvailability = ProductAvailability.IN_STOCK
                )
                updateInventory(updatedProduct)
                return Response.RESTORED_TO_INVENTORY

            }

        }
        return Response.INVALID_VALUE_PASSED
    }
}

