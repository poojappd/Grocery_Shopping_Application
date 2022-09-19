package com.example.groceryshoppingapplication

import androidx.room.Entity

@Entity
data class CartItem(
    val productCode: Int,
    val cartId: Int,
    var quantity: Int = 1
)

