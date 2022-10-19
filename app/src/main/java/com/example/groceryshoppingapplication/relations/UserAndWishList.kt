package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.models.WishListEntity

data class UserAndWishList(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val wishListEntity: WishListEntity
)
