package com.example.groceryshoppingapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey val userId: String,
    var mobileNumber: Number,
    var address: Address? = null) {


}
