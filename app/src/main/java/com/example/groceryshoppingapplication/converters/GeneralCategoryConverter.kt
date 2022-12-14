package com.example.groceryshoppingapplication.converters

import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.TypeConverter
import com.example.groceryshoppingapplication.enums.GeneralCategory

class GeneralCategoryConverter {
    @TypeConverter
    fun fromGeneralCategory(generalCategory: GeneralCategory) = generalCategory.value

    @TypeConverter
    fun toGeneralCategory(value: String): GeneralCategory = getEnum(value)

    fun getEnum(value: String): GeneralCategory {
        val trimmedValue = value.trim()
        for (i in GeneralCategory.values()) {
            if (i.value == trimmedValue)
                return i
        }
        Log.e(TAG,"NO ARGUMENT FOUNMD $value")
        throw IllegalArgumentException("No such enum constant?? -$value-")

    }
}