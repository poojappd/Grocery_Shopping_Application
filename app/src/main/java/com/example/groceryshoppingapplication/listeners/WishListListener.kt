package com.example.groceryshoppingapplication.listeners

import android.content.Context
import com.example.groceryshoppingapplication.WishListItemData

interface WishListListener {
    fun removeFromWishList(wishListItemId:Int)
    fun getWishListItemExtras(productCode: Int): WishListItemData
    fun getContext():Context
    fun navigate(productCode: Int)
}