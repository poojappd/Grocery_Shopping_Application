package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WishListItemEntity(
    val productCode: Int,
    val wishListId: Int,
    @PrimaryKey(autoGenerate = true)
    val id:Int = 1
)

