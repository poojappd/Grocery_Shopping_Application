package com.example.groceryshoppingapplication.converters

import android.content.ContentValues
import android.util.Log
import androidx.room.TypeConverter
import com.example.groceryshoppingapplication.enums.OrderStatus

class OrderStatusConverter {
    @TypeConverter
    fun fromOrderStatus(orderStatus: OrderStatus) = orderStatus.value

    @TypeConverter
    fun toOrderStatus(value: String) = getEnum(value)

    private fun getEnum(value: String): OrderStatus {
        for (i in OrderStatus.values()) {
            if (i.value == value)
                return i
        }
        Log.e(ContentValues.TAG,"NO ARGUMENT FOUNMD $value")
        throw IllegalArgumentException("No such enum constant?? $value")

    }
}