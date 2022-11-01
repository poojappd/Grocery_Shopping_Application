package com.example.groceryshoppingapplication.Utils

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.groceryshoppingapplication.data.AppDatabase
import kotlin.properties.Delegates


class MyGroceryApplication : Application() {
    val instance = this
    companion object {
       // var cartId: Int? = null
        lateinit var preferences: SharedPreferences
        var cartId = MutableLiveData<Int>()

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