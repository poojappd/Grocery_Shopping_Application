package com.example.groceryshoppingapplication.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.GroceryItemEntity

class SearchSuggestionViewModel(applicationContext: Context):ViewModel() {
    private val inventoryDAO = AppDatabase.getDatabase(applicationContext).getInventoryDao()
    val searchSuggestions:LiveData<List<GroceryItemEntity>>
        get() = _searchSuggestions
    private val _searchSuggestions = MutableLiveData<List<GroceryItemEntity>>()
    val searchQuery = MutableLiveData<String>()

    fun setSuggestion(query: String){
       _searchSuggestions.value =  inventoryDAO.searchInventory(query).value ?: listOf()
    }
}

class SearchSuggestionViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return SearchSuggestionViewModel(
            applicationContext
        ) as T
    }
}