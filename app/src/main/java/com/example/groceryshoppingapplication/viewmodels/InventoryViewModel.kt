package com.example.groceryshoppingapplication.viewmodels

import android.content.Context
import androidx.lifecycle.*
import androidx.room.Query
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.repositories.InventoryRepository

class InventoryViewModel(applicationContext: Context): ViewModel() {

    private val repo = InventoryRepository(AppDatabase.getDatabase(applicationContext))
    val allProductsInInventory = repo.productsInInventory

    fun getProduct(productCode:Int): LiveData<GroceryItemEntity> {
       return repo.getProductDetails(productCode)
    }

    fun getProductDetailsSynchronously(productCode: Int): GroceryItemEntity =
        repo.getProductDetailsSynchronously(productCode)

    fun getProductsUnderSubCategory(subCategory: SubCategory) = repo.getProductsUnderSubCategory(subCategory)

    fun getProductsUnderGeneralCategory(category: GeneralCategory) = repo.getProductsUnderGeneralCategory(category)

    fun searchProducts(query: String) = repo.searchProducts(query)

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