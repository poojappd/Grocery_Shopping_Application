package com.example.groceryshoppingapplication.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.models.Address
import java.util.*

class OrderDetailsViewModel (applicationContext: Context): ViewModel() {
    var totalItems:Int? = null
    var subTotal:Double? = null
    var deliveringAddress:Address? = null
    var receiverName:String? = null
    var paymentOption:String? = null
    var deliverySlot: Date? =null
}
class OrderDetailsViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return OrderDetailsViewModel(
            applicationContext
        ) as T
    }
}