package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class ModifiedOrderEntity(
    val orderId:String,
    val userId:String,
    val modifiedTime:String,
    @PrimaryKey
    val id:Int
)
