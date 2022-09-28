package com.example.groceryshoppingapplication.models

data class Address(
    var pincode: String,
    var houseNo: String,
    var streetDetails: String,
    var landmark: String?,
    var areaDetails: String,
    var city: String,
    var addressTag: String? = "Home"
)