package com.example.groceryshoppingapplication.models

import android.app.ActivityManager
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.groceryshoppingapplication.enums.*

@Entity(tableName = "Inventory")
data class GroceryItemEntity(
    @PrimaryKey val productCode: Int,
    val itemName: String,
    val brandName:String,
    val category: GeneralCategory,//enum
    val subCategory: SubCategory,//enum
    val packaging: Packaging,
    val capacityUnit: MeasuringUnit, //ml or g
    val capacity: Double,
    val unitPrice: Double,
    val productAvailability: ProductAvailability,
    val minimumQuantity:Int = 1,
    val availableQuantity:Int,
    val itemDescription: String,
)

