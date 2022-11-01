package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DefaultAddressEntity(
    val userId:String,
    val addressId:String,
    @PrimaryKey
    val id:Int
)
