package com.example.groceryshoppingapplication

class Cart(val userId:Int) {

    val totalItemsInCart:Int = 0
    val groceryItems:HashMap<Int, GroceryItem> = hashMapOf()


    fun addToCart(groceryItem: GroceryItem){
        groceryItems.put(groceryItem.itemId, groceryItem)
    }

    fun removeFromCart(itemId:Int, quantity:Int = 0){
        groceryItems.
    }

}