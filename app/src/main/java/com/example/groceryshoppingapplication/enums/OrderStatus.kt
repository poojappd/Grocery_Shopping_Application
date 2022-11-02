package com.example.groceryshoppingapplication.enums

enum class OrderStatus(val value:String) {
    COMPLETE("Complete"),
    CANCELLED("Cancelled"),
    ORDERED("Placed"),
    MODIFIED("Modified")
}