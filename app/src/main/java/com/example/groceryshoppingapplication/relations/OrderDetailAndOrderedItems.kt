package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.models.OrderDetail

data class OrderDetailAndOrderedItems (
    @Embedded val order: OrderDetail,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val orderedItems: List<OrderedItemEntity>

)