package com.example.groceryshoppingapplication.listeners

import android.content.Context
import android.view.View
import com.example.groceryshoppingapplication.enums.SubCategory

interface CategoryItemTouchListener {
    fun navigateToFragment(subCategory: SubCategory)
    fun getContext():Context
}