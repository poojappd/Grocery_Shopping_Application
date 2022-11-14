package com.example.groceryshoppingapplication.enums

enum class Response(val message:String) {
    NOT_LOGGED_IN("No user is logged in"),
    ITEM_PRESENT_IN_CART("Item is in cart"),
    MOBILE_NUMBER_NOT_VALID("Invalid mobile number"),
    MOBILE_NUMBER_NOT_ENTERED("Enter your mobile number"),
    MOBILE_NUMBER_LENGTH_SHORT("Mobile number should be 10 digits"),
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
    PIN_CODE_LENGTH_SHORT("Pincode should be 6 digits"),
    FIELD_REQUIRED("This field is required"),
    LANDMARK_INVALID("landmark is invalid"),
    CITY_NOT_VALID("Choose a city from the drop down"),
    HOUSE_NO_ALLOWED_SPL_CHARACTERS("Allowed special characters: dot (.), apostrophe ('), hyphen (-), slash (/ or \\), and spaces"),
    AREA_ALLOWED_SPL_CHARACTERS("Allowed special characters: dot (.), apostrophe ('), hyphen (-), ampersand (&), comma(,), slash (/ or \\), and spaces"),
    LETTER_NOT_FOUND("Atleast one letter should be present"),
    NO_NOT_FOUND("Atleast one number should be present"),
    VALIDATION_PASSED("Field is valid"),
    CONSECUTIVE_SPECIAL_CHARACTERS("Not more than 3 consecutive special characters are allowed"),
    STREET_INVALID("Street detail is invalid"),
    AREA_INVALID("Area detail is invalid"),
    FNAME_INVALID("First name is invalid"),
    LNAME_INVALID("Last name is invalid"),





}