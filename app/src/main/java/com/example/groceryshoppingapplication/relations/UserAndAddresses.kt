package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.User

data class UserAndAddresses(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val addresses: List<Address> = listOf()
)
