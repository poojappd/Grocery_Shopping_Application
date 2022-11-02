package com.example.groceryshoppingapplication.models

import androidx.room.PrimaryKey

data class ModifiedOrderEntity(
    val orderId:String,
    val userId:String,
    val modifiedTime:String,
    @PrimaryKey
    val id:Int
)
