package com.example.groceryshoppingapplication.repositories

import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.data.CartDAO
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.CartItemEntity

class CartRepository(database: AppDatabase) {
    private val cartDAO: CartDAO = database.getCartDao()
    suspend fun createCartForNewUser(cart:CartEntity) = cartDAO.createCart(cart)

    suspend fun getCartItemsFromCart(cartId:Int) = cartDAO.getCartItemsFromCart(cartId)

    suspend fun increaseQuantity(cartItemEntity: CartItemEntity) = cartDAO.increaseQuantity(cartItemEntity.productCode, cartItemEntity.cartId)


    suspend fun addToCart(cartItemEntity: CartItemEntity){
            cartDAO.addToCart(cartItemEntity)
    }


    fun getCartItem(productCode:Int, cartId:Int) = cartDAO.getCartItem(productCode, cartId)

    fun getCartItemQuantity(productCode: Int, cartId: Int) = cartDAO.getCartItemQuantity(productCode,cartId)
    suspend fun removeFromCart(cartItemEntity: CartItemEntity) = cartDAO.removeFromCart(cartItemEntity.cartId, cartItemEntity.productCode)

    suspend fun decreaseQuantity(cartItemEntity: CartItemEntity) {
       cartDAO.decreaseQuantity(cartItemEntity.productCode, cartItemEntity.cartId)
    }



    suspend fun emptyCart(cartId:Int) = cartDAO.emptyCart(cartId)




}