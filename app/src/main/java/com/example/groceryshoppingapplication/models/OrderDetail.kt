package com.example.groceryshoppingapplication.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.groceryshoppingapplication.DeliverySlot
import com.example.groceryshoppingapplication.Utils.DateTimeFormatter.formatDate
import com.example.groceryshoppingapplication.enums.OrderStatus
import java.text.DateFormat
import java.util.*

@Entity
data class OrderDetail(
    @PrimaryKey val orderId: String,
    val userId: String,
    val subTotal:Double,
    val orderDate: String = Date().formatDate("yyyy-MM-dd HH:mm"),
    val numberOfItems:Int,
    val deliverySlot: String,
    @Embedded(prefix = "add_")
    val deliveryAddress: Address,
    val mobileNumber: String,
    val deliveryCharges:Double,
    val totalPrice: Double,

    val orderStatus: OrderStatus = OrderStatus.ORDERED,
)
