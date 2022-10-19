package com.example.groceryshoppingapplication.repositories

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.data.CartDAO
import com.example.groceryshoppingapplication.data.WishListDAO
import com.example.groceryshoppingapplication.models.WishListEntity
import com.example.groceryshoppingapplication.models.WishListItemEntity
import com.example.groceryshoppingapplication.relations.WishListAndWishListItem

class WishListRepository(database: AppDatabase) {
    private val wishListDAO: WishListDAO = database.getWishListDao()

    suspend fun addToWishList(wishListItemEntity: WishListItemEntity) = wishListDAO.addToWishList(wishListItemEntity)

    fun getWishListItem(productCode: Int, wishListId: Int): WishListItemEntity? = wishListDAO.getWishListItem(productCode, wishListId)

    suspend fun removeFromWishList(wishListId: Int,wishListItemId: Int ) = wishListDAO.removeFromWishList(wishListId, wishListItemId)

    suspend fun emptyWishList(wishListId: Int) = wishListDAO.emptyWishList(wishListId)

    suspend fun createWishList(wishList: WishListEntity) = wishListDAO.createWishList(wishList)

    suspend fun getLastId(wishListId: Int): Int? = wishListDAO.getLastId(wishListId)

    suspend fun getWishListItemsFromWishList(wishListId: Int): WishListAndWishListItem = wishListDAO.getWishListItemsFromWishList(wishListId)

}