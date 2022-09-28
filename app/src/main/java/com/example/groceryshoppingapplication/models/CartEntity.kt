package com.example.groceryshoppingapplication.models

import androidx.room.*

@Entity
data class CartEntity(
    val userId: String,
    @PrimaryKey val cartId: Int
    ) {
    var totalItemsInCart: Int = 0
}
