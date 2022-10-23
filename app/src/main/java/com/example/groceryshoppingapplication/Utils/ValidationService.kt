package com.example.groceryshoppingapplication.Utils

object ValidationService {
    fun validateName(name:String): Boolean {
        if(name.matches(Regex("^[^&!@#$%^\\[*()~`/{}|=_+:\";]*$")) && name[0].toString().matches(Regex("[a-zA-z]"))){
            if(name.split(" ").size -1 >1)
                return false
            if(name.split("'").size -1 >1)
                return false
            if(name.split(".").size -1 >2)
                return false
            if(name.split("-").size -1 >1)
                return false
            return true
        }
        return false
    }

    fun validateHouseNumberWithResponse(houseNo:String){

    }
}