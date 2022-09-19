package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.CartItem
import com.example.groceryshoppingapplication.models.GroceryItem

data class CartItemAndProduct(
    @Embedded val groceryItem: GroceryItem,
    @Relation(
        parentColumn = "productCode",
        entityColumn = "productCode"
    )
    val cartItem: CartItem
)
