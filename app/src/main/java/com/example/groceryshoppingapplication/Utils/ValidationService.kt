package com.example.groceryshoppingapplication.Utils

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

    fun validateHouseNumber(houseNo:String): Boolean {
        if(houseNo.matches(Regex("^[a-zA-Z0-9 .\\-\\\\/]*$")) &&houseNo[0].toString().matches(Regex("^[^\\-/]*$"))){
            if (houseNo.count{ "/".contains(it) } <=2 && houseNo.count{ "\\".contains(it) } <=2 && houseNo.count{ "-".contains(it) } <=2 && (houseNo.any { c -> c.isDigit()})){
                return true
            }
        }
        return false
    }

    fun validateArea(area:String):Boolean{
        if (area.matches(Regex("^[a-zA-Z0-9 \\-\\\\/,.&]*$"))) {
            if (area.count { "/".contains(it) } <= 2 && area.count { "\\".contains(it) } <= 2 && area.count {
                    "-".contains(
                        it
                    )
                } <= 2 && area.count { "&".contains(it) } <= 2 && area.any { c -> c.isLetter() }) {
                return true
            }
        }
        return false

    }

    fun validateMobileNumber(mobile:String):Boolean{
        if( mobile.any { c -> c.isLetter() } || mobile.length !=10 )
            return false
        return true
    }
}