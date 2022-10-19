package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WishListEntity(
    val userId: String,
    @PrimaryKey val wishListId: Int
)
