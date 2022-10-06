package com.example.groceryshoppingapplication

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

interface CartItemTouchListener {
    fun addToCart(productCode:Int)
    fun removeFromCart(productCode: Int)
    fun getCartItemExtraData(productCode: Int):CartItemData
}