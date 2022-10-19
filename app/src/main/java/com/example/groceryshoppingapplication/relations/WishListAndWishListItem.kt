package com.example.groceryshoppingapplication.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.groceryshoppingapplication.models.WishListEntity
import com.example.groceryshoppingapplication.models.WishListItemEntity

data class WishListAndWishListItem(
    @Embedded
    val wishListEntity: WishListEntity,
    @Relation(
        parentColumn = "wishListId",
        entityColumn = "wishListId"
    )
    val wishListItemEntity: List<WishListItemEntity> = listOf()


)


