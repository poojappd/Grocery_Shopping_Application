package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data
class demoUser (
    val name:String,
    @PrimaryKey
    val id:Int = 1
)