package com.example.groceryshoppingapplication.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var userId:String,
    var mobileNumber: String,

) {
    var firstName: String? = null
    var lastName:String? = null
    var dob:String? = null



}
