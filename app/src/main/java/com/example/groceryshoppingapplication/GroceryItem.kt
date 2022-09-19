package com.example.groceryshoppingapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.MeasuringUnit
import com.example.groceryshoppingapplication.enums.ProductAvailability
import com.example.groceryshoppingapplication.enums.SubCategory

@Entity(tableName = "Inventory")
data class GroceryItem(
    @PrimaryKey val productCode: String,
    val itemName: String,
    val itemImage: Int,
    val category: GeneralCategory,
    val subCategory: SubCategory,
    val unit: MeasuringUnit, //ml or g
    val unitValue: Double,
    val unitPrice: Double,
    val availability: ProductAvailability,
    val minimumQuantity:Int = 1,
)
