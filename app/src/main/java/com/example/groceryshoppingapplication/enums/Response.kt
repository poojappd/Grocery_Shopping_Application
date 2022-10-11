package com.example.groceryshoppingapplication.enums

enum class Response(val message:String) {
    NOT_LOGGED_IN("No user is logged in"),
    ITEM_PRESENT_IN_CART("Item is in cart"),
    MOBILE_NUMBER_NOT_VALID("Invalid mobile number"),
    MOBILE_NUMBER_NOT_ENTERED("Mobile number not entered"),
    MOBILE_NUMBER_LENGTH_SHORT("Mobile number length is short"),
    MOBILE_NUMBER_VALID("Valid mobile number"),
    ITEM_ADDED_TO_CART("Added to cart!"),
    ITEM_REMOVED_FROM_CART("Item sucessfully removed from cart"),
    NO_SUCH_ITEM_IN_CART("Item not found in cart"),
    ITEMS_NOT_AVAILABLE_IN_STOCK("Some items are out of stock"),
    ITEMS_AVAILABLE_IN_STOCK("Items in stock"),
    ITEMS_REMOVED_FROM_INVENTORY("Item removed from inventory"),
    INSUFFICIENT_QUANTITY_IN_INVENTORY("Given quantity exceeds that of inventory"),
    NO_SUCH_ITEM_IN_INVENTORY("No such items in inventory"),
    RESTORED_TO_INVENTORY("Inventory has been restored"),
    INVALID_VALUE_PASSED("Invalid value has been passed"),
    NO_SUCH_USER("No users found"),
    LOGGED_IN_SUCCESSFULLY("Logged in successfully"),
    PIN_CODE_REQUIRED("Pin code is required"),
    HOUSE_NUMBER_REQUIRED("Enter your house number"),
    CITY_NOT_VALID("Enter a valid city name")




}