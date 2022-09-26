package com.example.groceryshoppingapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

@Entity
data class DeliverySlot(
    val Date:Date) {
    @PrimaryKey val id:Int = 0



}