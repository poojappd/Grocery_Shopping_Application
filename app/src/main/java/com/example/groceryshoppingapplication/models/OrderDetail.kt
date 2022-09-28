package com.example.groceryshoppingapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.groceryshoppingapplication.Utils.DateTimeFormatter.formatDate
import java.text.DateFormat
import java.util.*

@Entity
data class OrderDetail(
            @PrimaryKey val orderId: String,
            val userId: Int,
            val totalPrice: Double,
            val createdAt: String = Date().formatDate("yyyy-MM-dd HH:mm"),
            val modifiedAt: String = Date().formatDate("yyyy-MM-dd HH:mm"),
            val isDelivered: Boolean = false,
           )
