package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderedItemEntity (
    val orderId: String,
    val productCode: Int,
    val quantity: Int = 1,
    @PrimaryKey
    val id:String

)