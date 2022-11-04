package com.example.groceryshoppingapplication.Utils

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class MyGroceryApplication : Application() {
    val instance = this
    companion object {
       // var orderId: Int? = null
        lateinit var preferences: SharedPreferences
        var cartId = MutableLiveData<Int>()
        private var _modifiedStateEnabled = MutableLiveData<Boolean>(false)
        private var _modifiedStateOrderId = MutableLiveData<String>()
        val modifiedStateEnabled:LiveData<Boolean>
            get() = _modifiedStateEnabled
        val modifiedStateOrderId:LiveData<String>
        get() = _modifiedStateOrderId

        fun setModifiedStateEnabled(modifyStatusEnabled:Boolean, orderId:String){
                _modifiedStateEnabled.value = modifyStatusEnabled
                _modifiedStateOrderId.value = orderId
        }


    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "APP ON CREATE")
        preferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        cartId.value  = preferences.getInt("loggedUserCartId", -1)
        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            cartId.value = sharedPreferences.getInt("loggedUserCartId", -1)
            Log.e(TAG,"SHARED PREF CHANGED --- $cartId")
        }

    }


}