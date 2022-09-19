package com.example.groceryshoppingapplication

import androidx.room.Entity

@Entity
data class ProductCodeMap(val productCode:Int, val productName:String)