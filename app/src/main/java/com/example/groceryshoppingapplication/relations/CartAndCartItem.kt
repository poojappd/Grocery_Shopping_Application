package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.Cart
import com.example.groceryshoppingapplication.models.CartItem

data class CartAndCartItem(
    @Embedded
    val cart: Cart,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "cartId"
    )
    val cartItem: CartItem
)
