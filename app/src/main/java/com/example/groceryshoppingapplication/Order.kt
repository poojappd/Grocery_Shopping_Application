package com.example.groceryshoppingapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.groceryshoppingapplication.Utils.DateTimeFormatter.formatDate
import java.text.DateFormat
import java.util.*

@Entity
class Order(val cartId:Int,
            @PrimaryKey val orderId: String,
            val userId: Int,
            val total: Double,
            val createdAt: String = Date().formatDate("yyyy-MM-dd HH:mm"),
            val modifiedAt: String = Date().formatDate("yyyy-MM-dd HH:mm"),
            val isDelivered: Boolean = false,
           )
