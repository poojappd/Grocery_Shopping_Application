package com.example.groceryshoppingapplication.Utils

import kotlin.random.Random

object CodeGeneratorUtil {
    private var productCode = "001"
    private var userCode = "001"
    private val userCodePrefix = "#user_"


    fun generateOtp() = Random.nextInt(1000, 9999).toString()

    fun productCodeGenerator(itemName: String):String{
        val trimmedWord = itemName.replace(" ","")
        var pdCode:String = ""
        for(letter:Char in trimmedWord){
            pdCode += letter.code
        }
        return pdCode
    }

    fun userIdGenerator():String{
        val newUserCode = userCodePrefix + userCode
        val nextCodeValue = (userCode.toInt()+1)
        userCode = (if (nextCodeValue <10) "00$nextCodeValue" else if (nextCodeValue<100) "0$nextCodeValue" else "$nextCodeValue")
        return newUserCode
    }


}

