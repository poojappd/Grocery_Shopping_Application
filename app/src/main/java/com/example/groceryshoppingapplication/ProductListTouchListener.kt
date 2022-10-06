package com.example.groceryshoppingapplication

import com.example.groceryshoppingapplication.enums.Response

interface ProductListTouchListener {
    fun addToCart(productCode:Int)
    fun removeFromCart(productCode: Int)
    fun checkItemInCart(productCode: Int):Response
    fun navigate(productCode: Int)

}