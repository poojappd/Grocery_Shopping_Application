package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModifiedOrderItemEntity(
    val productCode: Int,
    val orderId: String,
    var quantity: Int = 1,
    @PrimaryKey
    val id:String
)
