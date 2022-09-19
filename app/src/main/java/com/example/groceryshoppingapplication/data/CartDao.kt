package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.CartItem
import com.example.groceryshoppingapplication.CartItemAndProduct

@Dao
interface CartDao{

    @Insert
    suspend fun addToCart(cartItem: CartItem)

    @Query("update CartItem set quantity = quantity + 1 where productCode = :productCode and cartId=:cartId")
    suspend fun increaseQuantity(productCode: Int, cartId: Int)

    @Query("update CartItem set quantity = quantity - 1 where productCode = :productCode and cartId=:cartId")
    suspend fun decreaseQuantity(productCode: Int, cartId: Int)

    @Query("select quantity from CartItem where productCode = :productCode and cartId = :cartId")
    suspend fun getQuantity(productCode: Int, cartId: Int)

    @Delete
    suspend fun removeFromCart(cartItem: CartItem)

    @Query("delete from CartItem where cartId = :cartId")
    suspend fun emptyCart(cartId: Int)

    @Transaction
    @Query("select * from Inventory ")
    fun getCartItems(cartId: Int): List<CartItemAndProduct>

}