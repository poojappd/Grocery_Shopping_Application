package com.example.groceryshoppingapplication.models

import androidx.room.Entity

@Entity
data class GroceryItemImageEntity(val productCode:String, val imageOrder:Int, val imagePath:String)
