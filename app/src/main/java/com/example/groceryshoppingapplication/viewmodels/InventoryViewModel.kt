package com.example.groceryshoppingapplication.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Query
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.repositories.InventoryRepository
import kotlinx.coroutines.launch

class InventoryViewModel(applicationContext: Context): ViewModel() {

    private val repo = InventoryRepository(AppDatabase.getDatabase(applicationContext))
    val allProductsInInventory = repo.productsInInventory

    fun getProduct(productCode: Int): LiveData<GroceryItemEntity> {
        return repo.getProductDetails(productCode)
    }

    fun getProductDetailsSynchronously(productCode: Int): GroceryItemEntity =
        repo.getProductDetailsSynchronously(productCode)

    fun getProductsUnderSubCategory(subCategory: SubCategory) =
        repo.getProductsUnderSubCategory(subCategory)

    fun getProductsUnderGeneralCategory(category: GeneralCategory) =
        repo.getProductsUnderGeneralCategory(category)

    fun searchProducts(query: String) = repo.searchProducts(query)

    fun reserveProducts(orderItems: List<OrderedItemEntity>) {
        viewModelScope.launch {
            for (i in orderItems) {
                val response = repo.fetchProductFromInventory(i.productCode, i.quantity)
                Log.e(TAG, response.message + " in reserve Products")
            }
        }
    }
    fun restoreProductToInventory(productCodeQtyPair: List<Pair<Int, Int>>) {
        viewModelScope.launch {
            for (pair in productCodeQtyPair) {
                repo.restoreProductToInventory(pair.first,pair.second)
            }
        }
    }

}
class InventoryViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}