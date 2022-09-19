package com.example.groceryshoppingapplication

import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.GroceryItem

class Carts(val userId: Int) {

    private var totalItemsInCart: Int = 0
    private val groceryItems: HashMap<String, GroceryItem> = hashMapOf()
    private val cartItemQuantities: HashMap<String, Int> = hashMapOf()


    fun addToCart(groceryItem: GroceryItem) {
        val qty = cartItemQuantities.get(groceryItem.itemName)
        groceryItems.put(groceryItem.productCode, groceryItem)
        cartItemQuantities.put(groceryItem.productCode, (qty ?: 1))
        totalItemsInCart++
    }

    fun removeFromCart(productCode: String): Response {
        if (cartItemQuantities[productCode] != null) {
            if (cartItemQuantities[productCode]!! > 1) {
                cartItemQuantities[productCode] = cartItemQuantities[productCode]!! - 1
            } else {
                val q = cartItemQuantities.remove(productCode)
                groceryItems.remove(productCode)

            }
            totalItemsInCart--
            return Response.ITEM_REMOVED_FROM_CART
        }
        return Response.NO_SUCH_ITEM_IN_CART

    }

    fun emptyCartItems(){
        groceryItems.clear()
        cartItemQuantities.clear()
        totalItemsInCart = 0
    }

//    fun checkCartItemAvailability():GroceryItem?{
//        val unavailableItems = mutableMapOf<GroceryItem,Int>()
//        for(item : GroceryItem in groceryItems.values){
//            val itemsInStock = Inventory.getItemCount(item.productCode)
//            if( itemsInStock < cartItemQuantities[item.productCode]!!){
//                unavailableItems.put(item)
//            }
//
//
//        }
//    }

}