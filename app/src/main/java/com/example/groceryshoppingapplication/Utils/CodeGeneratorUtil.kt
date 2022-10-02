package com.example.groceryshoppingapplication.Utils

import kotlin.random.Random

object CodeGeneratorUtil {
    private var productCode = "001"
    private var cartCode = 1002
    private var userCode = 2
    private val userCodePrefix = "#user_A"


    fun generateOtp() = Random.nextInt(1000, 9999).toString()

    fun productCodeGenerator(itemName: String):String{
        val trimmedWord = itemName.replace(" ","")
        var pdCode:String = ""
        for(letter:Char in trimmedWord){
            pdCode += letter.code
        }
        return pdCode
    }

    fun generateUserId():String{
        userCode++
        val appendString = (if (userCode <10) "00$userCode" else if (userCode<100) "0$userCode" else "$userCode")
        val newUserCode = userCodePrefix + appendString
        return newUserCode
    }

    fun generateCartId():Int{
        return cartCode++
    }


}

