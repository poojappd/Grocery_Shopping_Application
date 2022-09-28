package com.example.groceryshoppingapplication

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.repositories.InventoryRepository
import kotlinx.coroutines.launch

class SharedViewModel(applicationContext: Context): ViewModel() {

    private val repo = InventoryRepository(AppDatabase.getDatabase(applicationContext).getInventoryDao())
    val allProductsInInventory = repo.productsInInventory
    fun getProduct(productCode:Int): LiveData<GroceryItemEntity> {
       return repo.getProduct(productCode)
    }

    //val allUsers =


}

class SharedViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}