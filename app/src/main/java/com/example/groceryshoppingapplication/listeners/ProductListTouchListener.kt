package com.example.groceryshoppingapplication.listeners

import com.example.groceryshoppingapplication.enums.Response

interface ProductListTouchListener {
    fun addToCart(productCode:Int):Boolean
    fun removeFromCartCompletely(productCode: Int): Boolean
    fun checkItemInCart(productCode: Int):Response
    fun navigate(productCode: Int)
    fun addToWishList(productCode: Int):Boolean
    fun checkInWishList(productCode:Int):Boolean

}