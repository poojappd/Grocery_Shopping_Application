package com.example.groceryshoppingapplication.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.Address
import java.util.*

class DeliverySlotViewModel(applicationContext: Context): ViewModel() {
    var chosenTime: Date? = null
    val chosenAddress:Address? = null

}

class DeliverySlotViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return DeliverySlotViewModel(
            applicationContext
        ) as T
    }
}