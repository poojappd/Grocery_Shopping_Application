package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.relations.CartItemAndProduct

@Dao
interface CartDao{

    @Insert
    suspend fun addToCart(cartItemEntity: CartItemEntity)

    @Query("update CartItemEntity set quantity = quantity + 1 where productCode = :productCode and cartId=:cartId")
    suspend fun increaseQuantity(productCode: Int, cartId: Int)

    @Query("update CartItemEntity set quantity = quantity - 1 where productCode = :productCode and cartId=:cartId")
    suspend fun decreaseQuantity(productCode: Int, cartId: Int)

    @Query("select quantity from CartItemEntity where productCode = :productCode and cartId = :cartId")
    suspend fun getQuantity(productCode: Int, cartId: Int):Int

    @Delete
    suspend fun removeFromCart(cartItemEntity: CartItemEntity)

    @Query("delete from CartItemEntity where cartId = :cartId")
    suspend fun emptyCart(cartId: Int)

//    @Transaction
//    @Query("select * from Inventory where ")
//    fun getCartItems(cartId: Int): List<CartItemAndProduct>

}