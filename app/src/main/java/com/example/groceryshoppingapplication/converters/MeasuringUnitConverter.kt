package com.example.groceryshoppingapplication.converters

import android.content.ContentValues
import android.util.Log
import androidx.room.TypeConverter
import com.example.groceryshoppingapplication.enums.MeasuringUnit

class MeasuringUnitConverter {
    @TypeConverter
    fun fromMeasuringUnit(measuringUnit: MeasuringUnit) = measuringUnit.value

    @TypeConverter
    fun toMeasuringUnit(value:String) = getEnum(value)

    fun getEnum(value: String): MeasuringUnit {
        for (i in MeasuringUnit.values()) {
            if (i.value == value)
                return i
        }
        Log.e(ContentValues.TAG,"NO ARGUMENT FOUNMD $value")
        throw IllegalArgumentException("No such enum constant?? $value")

    }

}