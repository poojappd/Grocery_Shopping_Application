package com.example.groceryshoppingapplication.Other

import androidx.room.Entity
import com.example.groceryshoppingapplication.models.GroceryItem
import com.example.groceryshoppingapplication.enums.*

@Entity(tableName = "store_inventory")
object Inventory {
    private val inventoryItems = hashMapOf<String, GroceryItem>()
    private val itemQuantity = hashMapOf<String, Int>()

    fun restock(
        productCode: String,
        itemName: String,
        itemImage: Int,
        category: GeneralCategory,
        subCategory: SubCategory,
        unit: MeasuringUnit, //ml or g
        unitValue: Double,
        unitPrice: Double,
        availability: ProductAvailability,
        minimumQuantity: Int = 1,

        ) {
        val newItem = GroceryItem(
            productCode,
            itemName,
            itemImage,
            category,
            subCategory,
            unit, //ml or g
            unitValue,
            unitPrice,
            availability,
            minimumQuantity,
        )

        if (inventoryItems.containsKey(productCode)) {
            itemQuantity[productCode] = (itemQuantity[itemName]!! + minimumQuantity)
        } else {
            inventoryItems[productCode] = newItem
            itemQuantity[productCode] = minimumQuantity
        }

    }

    fun getItemCount(productCode: String): Int {
        return itemQuantity[productCode] ?: -1 //-1 indicates 'no such product'
    }

    fun getItem(productCode: String): GroceryItem? { //
        return inventoryItems[productCode]?.copy()
    }

    fun removeItem(
        productCode: String,
        quantity: Int
    ): Response { //called after checkout and payment
        if (itemQuantity.containsKey(productCode)) {
            if (itemQuantity[productCode]!! > quantity) {
                itemQuantity[productCode] = itemQuantity[productCode]!! - quantity

                return Response.ITEMS_REMOVED_FROM_INVENTORY
            } else if (itemQuantity[productCode] == quantity) {
                itemQuantity.remove(productCode)
                inventoryItems.remove(productCode)
                return Response.ITEMS_REMOVED_FROM_INVENTORY

            } else {
                return Response.INSUFFICIENT_QUANTITY_IN_INVENTORY
            }

        } else {
            return Response.NO_SUCH_ITEM_IN_INVENTORY
        }
    }


}
