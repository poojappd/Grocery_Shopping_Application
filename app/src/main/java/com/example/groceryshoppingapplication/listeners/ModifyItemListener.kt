package com.example.groceryshoppingapplication.listeners

import android.content.Context
import com.example.groceryshoppingapplication.WishListItemData
import com.example.groceryshoppingapplication.fragments.ModifyOrderFragment
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import java.text.DecimalFormat
import java.text.FieldPosition

interface ModifyItemListener {
        val decimal : DecimalFormat
        fun removeFromOrder(orderedItemEntity: OrderedItemEntity)
        fun getOrderedItemExtras(productCode: Int): ModifyOrderFragment.ModifyOrderItemData
        fun getContext(): Context
        fun decreaseQuantity(position: Int):Double
        fun increaseQuantity(position: Int):Double
        fun saveChanges()

}