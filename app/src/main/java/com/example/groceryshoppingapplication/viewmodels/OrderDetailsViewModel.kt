package com.example.groceryshoppingapplication.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Index
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.repositories.OrdersRepository
import java.util.*

class OrderDetailsViewModel(applicationContext: Context) : ViewModel() {
    var userId: String? = null
    var totalItems: Int? = null
    var subTotal: Double? = null
    var deliveringAddress: Address? = null
    var receiverName: String? = null
    var paymentOption: String? = null
    var deliverySlot: Date? = null
    var mobileNumber: String? = null

fun clearOrderDetails(){
    paymentOption = null
    userId   = null
    totalItems= null
      subTotal = null
      deliveringAddress = null
      receiverName   = null
      paymentOption   = null
      deliverySlot = null
      mobileNumber   = null

}
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