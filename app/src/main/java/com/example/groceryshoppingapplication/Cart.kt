package com.example.groceryshoppingapplication

import androidx.room.*

@Entity
data class Cart(val userId: String,@PrimaryKey val cartId: Int) {
    var totalItemsInCart: Int = 0
}
