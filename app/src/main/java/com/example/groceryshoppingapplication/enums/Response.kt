package com.example.groceryshoppingapplication.enums

enum class Response(val message:String) {
    MOBILE_NUMBER_NOT_VALID("Invalid mobile number"),
    MOBILE_NUMBER_NOT_ENTERED("Mobile number not entered"),
    MOBILE_NUMBER_LENGTH_SHORT("Mobile number length is short"),
    MOBILE_NUMBER_VALID("Valid mobile number"),
    ITEM_REMOVED_FROM_CART("Item sucessfully removed from cart"),
    NO_SUCH_ITEM_IN_CART("Item not found in cart"),
    ITEMS_NOT_AVAILABLE_IN_STOCK("Some items are out of stock"),
    ITEMS_AVAILABLE_IN_STOCK("Items in stock"),
    ITEMS_REMOVED_FROM_INVENTORY("Item removed from inventory"),
    INSUFFICIENT_QUANTITY_IN_INVENTORY("Given quantity exceeds that of inventory"),
    NO_SUCH_ITEM_IN_INVENTORY("No such items in inventory"),



}