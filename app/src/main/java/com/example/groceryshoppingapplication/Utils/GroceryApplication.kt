package com.example.groceryshoppingapplication.Utils

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

class MyGroceryApplication : Application() {
        val instance = this
}