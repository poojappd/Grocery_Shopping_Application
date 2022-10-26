package com.example.groceryshoppingapplication.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IdDAO {

    @Query("SELECT id FROM CartItemEntity where cartId = :cartId ORDER BY id DESC LIMIT 1")
    fun getLastCartItemId(cartId: Int): String?

    @Query("SELECT id FROM OrderedItemEntity where orderId = :orderId ORDER BY id DESC LIMIT 1")
    fun getLastOrderedItemId(orderId: String): String?

    @Query("select cartId from CartEntity order by cartId desc limit 1")
    fun getLastCartId():Int

    @Query("select wishListId from WishListEntity order by wishListId desc limit 1")
    fun getLastWishListId():Int

    @Query("SELECT id FROM WishListItemEntity where wishListId = :wishListId ORDER BY id DESC LIMIT 1")
    fun getLastWishListItemId(wishListId: Int): Int?

    @Query("select addressId from Address where userId=:userId order by addressId desc limit 1 ")
    fun getLastAddressId(userId:String): String?

    @Query("select userId from User order by userId desc limit 1")
    fun getLastUserId(): String

    @Query("select orderId from OrderDetail")
    fun getOrderIds():List<String>


}
