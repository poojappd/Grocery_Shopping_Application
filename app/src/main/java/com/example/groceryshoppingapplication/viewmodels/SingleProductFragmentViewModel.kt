package com.example.groceryshoppingapplication.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.data.CartDAO
import kotlinx.coroutines.launch

class SingleProductFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AppDatabase.getDatabase(application).getCartDao()
    val currentCart = application.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        .getInt("loggedUserCartId", -1)
    val currentProductCode = MutableLiveData<Int>()

    fun getCurrentProductCount() {
        val count = repo.getCartItemQuantity(
            currentProductCode.value!!,
            currentCart
        )
    }
}

