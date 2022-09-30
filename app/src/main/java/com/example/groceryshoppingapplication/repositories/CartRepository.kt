package com.example.groceryshoppingapplication.repositories

import com.example.groceryshoppingapplication.data.CartDAO
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.models.GroceryItemEntity

class CartRepository(private val cartDAO: CartDAO) {

    suspend fun createCartForNewUser(cart:CartEntity) = cartDAO.createCart(cart)

    suspend fun addToCart(cartItemEntity: CartItemEntity){
        val items = cartDAO.getCartItemsFromCart(cartItemEntity.cartId)
        var itemInCart = false
        for (i in items.cartItemEntity){
            if (i.productCode == cartItemEntity.productCode) {
                cartDAO.increaseQuantity(cartItemEntity.productCode, cartItemEntity.cartId)
                itemInCart = true
                break
            }
        }
        if(!itemInCart){
            cartDAO.addToCart(cartItemEntity)
        }

    }


    suspend fun removeFromCart(cartItemEntity: CartItemEntity) = cartDAO.removeFromCart(cartItemEntity)

    suspend fun decreaseQuantity(cartItemEntity: CartItemEntity) {
        if(cartDAO.getCartItemQuantity(cartItemEntity.productCode, cartItemEntity.cartId) > 1)
            cartDAO.decreaseQuantity(cartItemEntity.productCode, cartItemEntity.cartId)
        else
            cartDAO.removeFromCart(cartItemEntity)


    }



    suspend fun emptyCart(cartId:Int) = cartDAO.emptyCart(cartId)

    suspend fun getUserCartItems(cartId: Int) = cartDAO.getCartItemsFromCart(cartId)



}