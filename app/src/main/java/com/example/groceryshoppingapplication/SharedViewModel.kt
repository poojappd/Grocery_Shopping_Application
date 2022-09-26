package com.example.groceryshoppingapplication

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.repositories.InventoryRepository

class SharedViewModel(): ViewModel() {

    private val repo = InventoryRepository(AppDatabase.getDatabase(MyGroceryApplication().instance).getInventoryDao())
    val allProductsInInventory = repo.productsInInventory
}

class SharedViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}