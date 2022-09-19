package com.example.groceryshoppingapplication

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemAndProduct(
    @Embedded val groceryItem: GroceryItem,
    @Relation(
        parentColumn = "productCode",
        entityColumn = "productCode"
    )
    val cartItem: CartItem
)
