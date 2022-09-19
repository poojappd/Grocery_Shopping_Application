package com.example.groceryshoppingapplication

import androidx.room.Embedded
import androidx.room.Relation

data class CartAndCartItem(
    @Embedded
    val cart: Cart,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "cartId"
    )
    val cartItem: CartItem
)
