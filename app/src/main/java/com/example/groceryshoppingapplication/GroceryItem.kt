package com.example.groceryshoppingapplication

data class GroceryItem(
    val itemId: Int,
    val category: Category,
    val itemName: String,
    val price: Double,
    val unit: MeasuringUnit,
    val unitValue: Double,
    val unitPrice: Double
)