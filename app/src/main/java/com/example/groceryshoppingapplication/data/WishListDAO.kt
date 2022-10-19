package com.example.groceryshoppingapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.groceryshoppingapplication.models.WishListEntity
import com.example.groceryshoppingapplication.models.WishListItemEntity
import com.example.groceryshoppingapplication.relations.WishListAndWishListItem

@Dao
interface WishListDAO {
    @Insert
    suspend fun addToWishList(WishListItemEntity: WishListItemEntity)

    @Query("select * from WishListItemEntity where wishListId = :wishListId and productCode = :productCode")
    fun getWishListItem(productCode: Int, wishListId: Int): WishListItemEntity?

    @Query("delete from WishListItemEntity where wishListId = :wishListId and id = :wishListItemId")
    suspend fun removeFromWishList(wishListId: Int,wishListItemId: Int)

    @Query("delete from WishListItemEntity where wishListId = :wishListId")
    suspend fun emptyWishList(wishListId: Int)

    @Insert
    suspend fun createWishList(cart: WishListEntity)

    @Query("SELECT id FROM WishListItemEntity where wishListId = :wishListId ORDER BY id DESC LIMIT 1;\n")
    suspend fun getLastId(wishListId: Int): Int?


    @Transaction
    @Query("select * from WishListEntity where wishListId = :wishListId")
    suspend fun getWishListItemsFromWishList(wishListId: Int): WishListAndWishListItem



}

