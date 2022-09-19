package com.example.groceryshoppingapplication

data class Address(
    var pincode: String,
    var houseNo: String,
    var streetDetails: String,
    var landmark: String,
    var areaDetails: String,
    var city: String,
    var addressTag: String = "Home"
)