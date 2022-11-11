package com.example.groceryshoppingapplication.Utils

import android.content.ContentValues.TAG
import android.text.TextUtils.substring
import android.util.Log
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication.Companion.cartId
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
    private var lastCartItemId :Int? = null

    fun setIdDao(idDAO: IdDAO) {
        this.idDAO = idDAO

    }

    fun generateOtp() = Random.nextInt(1000, 9999).toString()

    fun productCodeGenerator(itemName: String): String {
        val trimmedWord = itemName.replace(" ", "")
        var pdCode: String = ""
        for (letter: Char in trimmedWord) {
            pdCode += letter.code
        }
        return pdCode
    }

    fun generateAddressId(userId: String): String {
        val prefix = "$userId/"
        val lastAddressId = idDAO.getLastAddressId(userId)
        val newAddressId = lastAddressId?.let {
            it.substring(lastAddressId.indexOf("/").plus(1)).trim().toInt() + 1
        } ?: 1
        return "$prefix$newAddressId"
    }


    fun generateUserId(): String {
        val userId = idDAO.getLastUserId()
        val usercode = userId.substring(userId.indexOf("A") + 1)
        val userCode = usercode.toInt() + 1
        val appendString =
            (if (userCode < 10) "00$userCode" else if (userCode < 100) "0$userCode" else "$userCode")
        val newUserCode = userCodePrefix + appendString
        return newUserCode
    }

    fun generateCartId(): Int {
        return idDAO.getLastCartId() + 1
    }

    fun generateCartItemId(cartId: Int): String {

        lastCartItemId = this.lastCartItemId?.plus(1) ?: idDAO.getLastCartItemId(cartId)
            val prefix = "$cartId/"
            Log.e(TAG, "Last cart Id : $lastCartItemId")
            val newCartItemId = lastCartItemId?.let {
                it + 1
            } ?: 1
            return "$prefix$newCartItemId"

    }


    fun generateOrderedItemId(orderId: String): String {
        val prefix = "$orderId/"
        val lastOrderedItemId = idDAO.getLastOrderedItemId(orderId)
        val newOrderItemId = lastOrderedItemId?.let {
            it.substring(lastOrderedItemId.indexOf("/").plus(1)).trim().toInt() + 1
        } ?: 1
        return "$prefix$newOrderItemId"
    }

    fun generateModifiedOrderedItemId(orderId: String): String {
        val prefix = "$orderId/"
        val lastOrderedItemId = idDAO.getLastModifiedOrderedItemId(orderId)
        val newOrderItemId = lastOrderedItemId?.let { it + 1 } ?: 1
        return "$prefix$newOrderItemId"
    }

    fun generateWishListItemId(): Int {
        return idDAO.getLastWishListItemId()?.plus(1) ?: 1
    }

    fun generateWishListId(): Int {
        return idDAO.getLastWishListId() + 1
    }

    fun generateOrderId(orderDate: Date): String {
        val ids = idDAO.getOrderIds()
        var newOrderId: String
        do {
            val prefix = Random.nextInt(100000, 1000000)
            val postfix = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(orderDate)
            newOrderId = "#$prefix-$postfix"
        } while (ids.contains(newOrderId))
        return newOrderId
    }

    fun generateLastDefaultAddressId(): Int = idDAO.getLastDefaultAddressId()?.plus(1) ?: 1

}

