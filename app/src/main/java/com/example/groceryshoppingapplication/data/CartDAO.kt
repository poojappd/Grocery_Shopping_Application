package com.example.groceryshoppingapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.relations.CartAndCartItem
import com.example.groceryshoppingapplication.relations.CartItemAndProduct

@Dao
interface CartDAO {

    @Insert
    suspend fun addToCart(cartItemEntity: CartItemEntity)

    @Query("update CartItemEntity set quantity = quantity + 1 where productCode = :productCode and cartId=:cartId")
    suspend fun increaseQuantity(productCode: Int, cartId: Int)

    @Query("update CartItemEntity set quantity = quantity - 1 where productCode = :productCode and cartId=:cartId")
    suspend fun decreaseQuantity(productCode: Int, cartId: Int)

    @Query("select * from CartItemEntity where cartId = :cartId and productCode = :productCode")
    suspend fun getCartItem(productCode: Int, cartId: Int): LiveData<CartItemEntity>


    //testing if quantity increases
    @Query("select quantity from CartItemEntity where cartId = :cartId and productCode = productCode")
    fun getCartItemQuantity(productCode: Int, cartId: Int): Int

    @Delete
    suspend fun removeFromCart(cartItemEntity: CartItemEntity)

    @Query("delete from CartItemEntity where cartId = :cartId")
    suspend fun emptyCart(cartId: Int)

    @Insert
    suspend fun createCart(cart: CartEntity)


    @Transaction
    @Query("select * from CartEntity")
    suspend fun getCartItemsFromCart(cartId: Int): CartAndCartItem




}