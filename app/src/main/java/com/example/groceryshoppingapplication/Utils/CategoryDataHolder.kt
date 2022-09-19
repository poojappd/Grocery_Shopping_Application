package com.example.groceryshoppingapplication.Utils

import android.content.Context
import com.example.groceryshoppingapplication.R

data class DataUtil(val context: Context) {
    val drawables =
        arrayOf(
            R.drawable.milk_icon,
            R.drawable.meat_icon,
            R.drawable.beverage_icon,
            R.drawable.staples_icon1,
            R.drawable.fruits_icon,
            R.drawable.vegetables_icon,
            R.drawable.hair_care_icon1,
            R.drawable.ic__skin_care_icon,
            R.drawable.snacks_icon_1,
            R.drawable.home_needs_icon1,
        )

    private val descriptions = context.resources.getStringArray(R.array.categories)

    val categoryDataHolders = mutableListOf<CategoryDataHolder>()
    init {

        for (i in drawables.indices) {
            categoryDataHolders.add(CategoryDataHolder(drawables[i], descriptions[i]))
        }
    }

}

class CategoryDataHolder(val iconId: Int, val description: String)