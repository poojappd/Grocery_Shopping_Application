package com.example.groceryshoppingapplication

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndCart(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val cart: Cart
)
