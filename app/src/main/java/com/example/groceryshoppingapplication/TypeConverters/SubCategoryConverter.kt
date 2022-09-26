package com.example.groceryshoppingapplication.TypeConverters

import android.content.ContentValues
import android.util.Log
import androidx.room.TypeConverter
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory

class SubCategoryConverter {
    @TypeConverter
    fun fromSubCategory(subCategory: SubCategory) = subCategory.value

    @TypeConverter
    fun toSubCategory(value: String): SubCategory = getEnum(value)

    fun getEnum(value: String): SubCategory {
        for (i in SubCategory.values()) {
            if (i.value == value)
                return i
        }
        Log.e(ContentValues.TAG,"NO ARGUMENT FOUNMD $value")
        throw IllegalArgumentException("No such enum constant?? $value")

    }
}