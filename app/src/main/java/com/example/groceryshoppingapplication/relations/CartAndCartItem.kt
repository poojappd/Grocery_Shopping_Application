package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.CartItemEntity

data class CartAndCartItem(
    @Embedded
    val cartEntity: CartEntity,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "cartId"
    )
    val cartItemEntity: CartItemEntity
)
