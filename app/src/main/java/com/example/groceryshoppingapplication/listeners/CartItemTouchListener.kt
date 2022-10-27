package com.example.groceryshoppingapplication.listeners

import com.example.groceryshoppingapplication.CartItemData

interface CartItemTouchListener {
    fun addToCart(productCode:Int)
    fun removeFromCart(productCode: Int)
    fun getCartItemExtraData(productCode: Int): CartItemData
    fun navigateToProduct(productCode: Int)
}