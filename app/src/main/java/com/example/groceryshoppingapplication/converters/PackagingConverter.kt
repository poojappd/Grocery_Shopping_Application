package com.example.groceryshoppingapplication.converters

import android.content.ContentValues
import android.util.Log
import androidx.room.TypeConverter
import com.example.groceryshoppingapplication.enums.Packaging

class PackagingConverter {
    @TypeConverter
    fun fromPackaging(packaging: Packaging) = packaging.value

    @TypeConverter
    fun toPackaging(value:String) = getEnum(value)

    private fun getEnum(value: String): Packaging {
        for (i in Packaging.values()) {
            if (i.value == value)
                return i
        }
        Log.e(ContentValues.TAG,"NO ARGUMENT FOUNMD $value")
        throw IllegalArgumentException("No such enum constant?? $value")

    }

}