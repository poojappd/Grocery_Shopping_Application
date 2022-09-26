package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.models.GroceryItemEntity

data class CartItemAndProduct(
    @Embedded val groceryItemEntity: GroceryItemEntity,
    @Relation(
        parentColumn = "productCode",
        entityColumn = "productCode"
    )
    val cartItemEntity: CartItemEntity
)
