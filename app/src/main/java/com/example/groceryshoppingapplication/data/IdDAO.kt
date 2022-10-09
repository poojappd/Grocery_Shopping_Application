package com.example.groceryshoppingapplication.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IdDAO {

    @Query("SELECT id FROM CartItemEntity where cartId = :cartId ORDER BY id DESC LIMIT 1")
    fun getLastCartItemId(cartId: Int): Int?

    @Query("select cartId from CartEntity order by cartId desc limit 1")
    fun getLastCartId():Int

    @Query("select addressId from Address where userId=:userId order by addressId desc limit 1 ")
    fun getLastAddressId(userId:String): Int?

    @Query("select userId from User order by userId desc limit 1")
    fun getLastUserId(): String

}
