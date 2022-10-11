package com.example.groceryshoppingapplication.Utils

import android.content.Context
import com.example.groceryshoppingapplication.ChildCategoryRowData
import com.example.groceryshoppingapplication.ParentCategoryRowData
import com.example.groceryshoppingapplication.R


data class CategoriesUtil(val context: Context) {
    private val categoryImages: Array<Int>
    private val categoryTexts: Array<String>
    private val dairy: Array<String>
    private val staples: Array<String>
    private val fruitsVeggies: Array<String>
    private val beverages: Array<String>
    private val snacks: Array<String>
    private val beauty: Array<String>
    private val household: Array<String>
    private val kitchen: Array<String>
    private val meat: Array<String>
    val parentCategoryArray = mutableListOf<ParentCategoryRowData>()
    val childCategoryArray : MutableList<MutableList<ChildCategoryRowData>>


    init {
        categoryImages = arrayOf(
            R.drawable.milk_icon,
            R.drawable.meat_icon,
            R.drawable.beverage_icon,
            R.drawable.staples_icon1,
            R.drawable.vegetables_icon,
            R.drawable.kitchen_icon1,
            R.drawable.ic__skin_care_icon,
            R.drawable.snacks_icon_1,
            R.drawable.home_needs_icon1,
        )
        categoryTexts = context.resources.getStringArray(R.array.categories)
        for (i in categoryImages.indices) {
            parentCategoryArray.add(ParentCategoryRowData(categoryImages[i], categoryTexts[i]))
        }

        dairy = context.resources.getStringArray(R.array.dairy_bakery)
        val dairyList = mutableListOf<ChildCategoryRowData>()
        for(i in dairy){
            dairyList.add(ChildCategoryRowData(i))
        }

        staples = context.resources.getStringArray(R.array.staples)
        val staplesList = mutableListOf<ChildCategoryRowData>()
        for(i in staples){
            staplesList.add(ChildCategoryRowData(i))
        }
        fruitsVeggies = context.resources.getStringArray(R.array.fruits_veggie)
        val fruitsList = mutableListOf<ChildCategoryRowData>()
        for(i in fruitsVeggies){
            fruitsList.add(ChildCategoryRowData(i))
        }
        beverages = context.resources.getStringArray(R.array.beverages)
        val beveragesList = mutableListOf<ChildCategoryRowData>()
        for(i in beverages){
            beveragesList.add(ChildCategoryRowData(i))
        }
        snacks = context.resources.getStringArray(R.array.snacks_packed_foods)
        val snacksList = mutableListOf<ChildCategoryRowData>()
        for(i in snacks){
            snacksList.add(ChildCategoryRowData(i))
        }

        beauty = context.resources.getStringArray(R.array.beauty_and_hygiene)
        val beautyList = mutableListOf<ChildCategoryRowData>()
        for(i in beauty){
            beautyList.add(ChildCategoryRowData(i))
        }
        household = context.resources.getStringArray(R.array.cleaning_and_household)
        val householdsList = mutableListOf<ChildCategoryRowData>()
        for(i in household){
            householdsList.add(ChildCategoryRowData(i))
        }
        kitchen = context.resources.getStringArray(R.array.kitchen_garden_pets)
        val kitchenList = mutableListOf<ChildCategoryRowData>()
        for(i in kitchen){
            kitchenList.add(ChildCategoryRowData(i))
        }
        meat = context.resources.getStringArray(R.array.meat_and_eggs)
        val meatList = mutableListOf<ChildCategoryRowData>()
        for(i in meat){
            meatList.add(ChildCategoryRowData(i))
        }
        childCategoryArray = mutableListOf(
            dairyList,
            meatList,
            staplesList,
            fruitsList,
            beveragesList,
            snacksList,
            beautyList,
            householdsList,
            kitchenList
        )


    }


}