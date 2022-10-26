package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    val userId:String,
    var pincode: String,
    var houseNo: String,
    var streetDetails: String,
    var landmark: String?,
    var areaDetails: String,
    var city: String,
    var addressTag: String? = "Home",
    @PrimaryKey
    val addressId:String

)