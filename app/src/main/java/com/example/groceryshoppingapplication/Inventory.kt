package com.example.groceryshoppingapplication

class Inventory(){
    val inventoryItems = arrayListOf<GroceryItem>()
    fun restock( itemId: Int, category: Category, itemName: String, price: Double,
                 unit: MeasuringUnit, unitValue: Double, unitPrice: Double)
    {
        inventoryItems.add(
            GroceryItem( itemId, category, itemName, price, unit, unitValue, unitPrice)
        )
    } 
}
