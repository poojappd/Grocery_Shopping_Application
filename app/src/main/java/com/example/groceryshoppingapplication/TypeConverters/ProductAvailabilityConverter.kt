package com.example.groceryshoppingapplication.TypeConverters

import androidx.room.TypeConverter
import com.example.groceryshoppingapplication.enums.ProductAvailability

class ProductAvailabilityConverter {
    @TypeConverter
    fun fromProductAvailability(productAvailability: ProductAvailability) = productAvailability.value

    @TypeConverter
    fun toProductAvailability(value:Int) =
        when(value){
            1 -> ProductAvailability.IN_STOCK
            else -> ProductAvailability.OUT_OF_STOCK
        }

}