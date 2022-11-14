package com.example.groceryshoppingapplication.Utils

import android.content.ContentValues.TAG
import android.util.Log
import com.example.groceryshoppingapplication.enums.Response
import java.util.regex.Pattern

object ValidationService {
    fun validateFirstName(name:String): Boolean {
        if(name.matches(Regex("^[^&!@#$%^\\[*()<>\\]~`/{}|=_+:\";]*$")) && name[0].toString().matches(Regex("[a-zA-z]"))){
            if(name.count { " ".contains(it) }  >3)
                return false
            if(name.count { "'".contains(it) }  >2)
                return false
            if(name.count { ".".contains(it) }  >3)
                return false
            if(name.count { "-".contains(it) }  >2)
                return false
            return true
        }
        return false
    }

    fun validateLastName(name:String): Boolean {
        if(name.matches(Regex("^[^&!@#$%^\\[*()<>\\]~`/{}|=_+:\";]*$"))){
            if(!name.any { c -> c.isLetter() })
                return false
            if(name.count { " ".contains(it) }  >3)
                return false
            if(name.count { "'".contains(it) }  >2)
                return false
            if(name.count { ".".contains(it) }  >3)
                return false
            if(name.count { "-".contains(it) }  >1)
                return false
            return true

            return true
        }
        return false
    }

    fun validateHouseNumber(houseNo:String): Response {
        if (!(houseNo.any { c -> c.isDigit()}))
            return Response.NO_NOT_FOUND
        if(!houseNo.matches(Regex("^[a-zA-Z0-9 .\\-\\\\/]*$")))
            return Response.HOUSE_NO_ALLOWED_SPL_CHARACTERS
        if(!Pattern.compile("^(?!.*[\\- ./]{4}).+$").matcher(houseNo).matches())
            return Response.CONSECUTIVE_SPECIAL_CHARACTERS
        return Response.VALIDATION_PASSED
    }


    fun validateArea(area:String):Response{
        Log.e(TAG,area+" "+ area.any { c -> c.isLetter()})
        if (!area.any { c -> c.isLetter()})
            return Response.LETTER_NOT_FOUND
        if (!area.matches(Regex("^[a-zA-Z0-9 \\-\\\\/,.&']*$")))
            return Response.AREA_ALLOWED_SPL_CHARACTERS
        if(!Pattern.compile("^(?!.*[\\- ./]{4}).+$").matcher(area).matches())
            return Response.CONSECUTIVE_SPECIAL_CHARACTERS
        return Response.VALIDATION_PASSED
    }

    fun validateMobileNumber(mobile:String):Boolean{
        if( mobile.any { c -> c.isLetter() } || mobile.length != 10 )
            return false
        return true
    }
}