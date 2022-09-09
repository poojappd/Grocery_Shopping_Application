package com.example.groceryshoppingapplication

enum class Response(val message:String) {
    MOBILE_NUMBER_NOT_VALID("Invalid mobile number"),
    MOBILE_NUMBER_NOT_ENTERED("Mobile number not entered"),
    MOBILE_NUMBER_LENGTH_SHORT("Mobile number length is short"),
    MOBILE_NUMBER_VALID("Valid mobile number")


}