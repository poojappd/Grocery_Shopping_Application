package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.User

data class UserAndOrders(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId",
    )
    val ordersInfo: List<OrderDetail> = listOf()
)
