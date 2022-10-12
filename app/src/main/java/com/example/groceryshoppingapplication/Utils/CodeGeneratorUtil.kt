package com.example.groceryshoppingapplication.Utils

import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil.idDAO
import com.example.groceryshoppingapplication.data.IdDAO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object CodeGeneratorUtil {
    private lateinit var idDAO: IdDAO
    private var productCode = "001"
    private var addressCode = 1
    private var cartCode = 1002
    private const val userCodePrefix = "#user_A"

    fun setIdDao(idDAO: IdDAO){
        this.idDAO = idDAO
    }

    fun generateOtp() = Random.nextInt(1000, 9999).toString()

    fun productCodeGenerator(itemName: String):String{
        val trimmedWord = itemName.replace(" ","")
        var pdCode:String = ""
        for(letter:Char in trimmedWord){
            pdCode += letter.code
        }
        return pdCode
    }

    fun generateAddressId(userId:String) =  idDAO.getLastAddressId(userId)?.plus(1) ?: 1


    fun generateUserId():String{
        val userId = idDAO.getLastUserId()
        val usercode = userId.substring(userId.indexOf("A")+1)
        val userCode = usercode.toInt()+1
        val appendString = (if (userCode <10) "00$userCode" else if (userCode<100) "0$userCode" else "$userCode")
        val newUserCode = userCodePrefix + appendString
        return newUserCode
    }

    fun generateCartId():Int{
       return idDAO.getLastCartId() + 1
    }

    fun generateCartItemId(cartId:Int):Int{
        return idDAO.getLastCartItemId(cartId)?.plus(1) ?: 1
    }


    fun generateOrderId(orderDate: Date):String{
        val ids = idDAO.getOrderIds()
        var newOrderId:String
        do {
            val prefix = Random.nextInt(100000,1000000)
            val postfix = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(orderDate)
            newOrderId = "#$prefix-$postfix"
        }while (ids.contains(newOrderId))
        return newOrderId
    }

}

