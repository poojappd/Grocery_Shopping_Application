package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItemEntity(
    val productCode: Int,
    val cartId: Int,
    var quantity: Int = 1,
    @PrimaryKey(autoGenerate = true)
    val id:Int = 1
)

