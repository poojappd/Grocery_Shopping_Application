package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.User

data class UserAndCart(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val cartEntity: CartEntity
)
