package com.example.groceryshoppingapplication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class SharedPrefViewModel(application: Application) :AndroidViewModel(application) {

    val sharedPref = application.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    var userMobile = sharedPref.getString("loggedUserMobile", null)
    val userCartId = sharedPref.getInt("loggedUserCartId", -1)


}

class SharedPrefViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedPrefViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedPrefViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}