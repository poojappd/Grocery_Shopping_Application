package com.example.groceryshoppingapplication.Utils

import android.content.Context
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory


data class CategoriesUtil(val context: Context) {
    val categoryImages: Array<Int>
//    private val categoryTexts: Array<String>
//    private val dairy: Array<String>
//    private val staples: Array<String>
//    private val fruitsVeggies: Array<String>
//    private val beverages: Array<String>
//    private val snacks: Array<String>
//    private val beauty: Array<String>
//    private val household: Array<String>
//    private val kitchen: Array<String>
//    private val meat: Array<String>
//    val parentCategoryArray = mutableListOf<ParentCategoryRowData>()
//    val childCategoryArray : MutableList<MutableList<ChildCategoryRowData>>

    val categoryMap: Map<GeneralCategory, List<SubCategory>> =
        mapOf(
            GeneralCategory.FRUITS to listOf(
                SubCategory.FRESH_FRUITS,
                SubCategory.EXOTIC_FRUITS,
                SubCategory.FLOWER_BOUQUETS_BUNCHES,
            ),
            GeneralCategory.VEGETABLES to listOf(
                SubCategory.HERBS_AND_SEASONINGS,
                SubCategory.FRESH_VEGETABLES,
                SubCategory.CUTS_AND_SPROUTS,
            ),
            GeneralCategory.DAIRY_AND_BAKERY to listOf(
                SubCategory.DAIRY,
                SubCategory.BREADS_AND_BUNS, SubCategory.TOAST_AND_BAKING_NEEDS,
                SubCategory.ICE_CREAMS_AND_DESSERTS,
                SubCategory.CAKES_AND_PASTRIES
            ),
            GeneralCategory.MEAT_AND_EGGS to listOf(
                SubCategory.EGGS,
                SubCategory.MUTTON_AND_LAMB,
                SubCategory.FISH_AND_SEAFOOD,
                SubCategory.SAUSAGES_BACON_AND_SALAMI
            ),
            GeneralCategory.BEVERAGES to listOf(
                SubCategory.ENERGY_AND_SOFT_DRINKS, SubCategory.HEALTH_DRINK_SUPPLEMENT,
                SubCategory.TEA,
                SubCategory.COFFEE,
                SubCategory.FRUIT_JUICES_AND_DRINKS,
                SubCategory.SYRUPS_POWDERS_AND_MIXES
            ),
            GeneralCategory.STAPLES to listOf(
                SubCategory.ATTA_FLOURS_AND_SOOJI,
                SubCategory.RICE_AND_RICE_PRODUCTS, SubCategory.DALS_AND_PULSES,
                SubCategory.EDIBLE_OILS_AND_GHEE,
                SubCategory.SALT_SUGAR_AND_JAGGERY,
                SubCategory.MASALAS_AND_SPICES,
                SubCategory.DRY_FRUITS
            ),
            GeneralCategory.SNACKS_AND_BRANDED_FOODS to listOf(
                SubCategory.NOODLES_PASTA_VERMICELLI,
                SubCategory.BREAKFAST_CEREALS,
                SubCategory.FROZEN_VEGGIES_AND_SNACKS,
                SubCategory.BISCUITS_AND_COOKIES,
                SubCategory.CHIPS_AND_CORN_SNACKS,
                SubCategory.SPREADS_SAUCES_KETCHUP,
                SubCategory.CHOCOLATES_AND_CANDIES,
                SubCategory.PICKLES_AND_CHUTNEY
            ),
            GeneralCategory.BEAUTY_AND_HYGIENE to listOf(
                SubCategory.FEMININE_HYGIENE, SubCategory.HEALTH_AND_MEDICINE,
                SubCategory.HAIR_CARE,
                SubCategory.SKIN_CARE, SubCategory.MAKEUP, SubCategory.FRAGRANCES_AND_DEOS,
                SubCategory.ORAL_CARE,
                SubCategory.BATH_AND_HAND_WASH
            ),
            GeneralCategory.CLEANING_AND_HOUSEHOLD to listOf(
                SubCategory.DETERGENTS_AND_DISHWASH, SubCategory.FRESHENERS_AND_REPELLENTS,
                SubCategory.MOPS_BRUSHES_AND_SCRUBS, SubCategory.ALL_PURPOSE_CLEANERS,
                SubCategory.STATIONERY,
                SubCategory.KITCHEN_ACCESSORIES
            ),
            GeneralCategory.GARDENING to listOf(
                SubCategory.GARDENING_TOOLS,
                SubCategory.SEEDS
            ),
            GeneralCategory.PETS to listOf(
                SubCategory.DOG_FOOD,
                SubCategory.CAT_FOOD,
                SubCategory.OTHER_PET_FOOD,
                SubCategory.PET_ACCESSORIES
            )
        )


    init {
        categoryImages = arrayOf(
            R.drawable.frui,
            R.drawable.vegetables_icon,
            R.drawable.milk_icon,
            R.drawable.meat_icon,
            R.drawable.beverage_icon,
            R.drawable.staples_icon1,
            R.drawable.snacks_icon_1,
            R.drawable.ic__skin_care_icon,
            R.drawable.home_needs_icon1,
            R.drawable.garden,
            R.drawable.pet1,

            )
//        categoryTexts = context.resources.getStringArray(R.array.categories)
//        for (i in categoryImages.indices) {
//            parentCategoryArray.add(ParentCategoryRowData(categoryImages[i], categoryTexts[i]))
//        }
//
//        dairy = context.resources.getStringArray(R.array.dairy_bakery)
//        val dairyList = mutableListOf<ChildCategoryRowData>()
//        for(i in dairy){
//            dairyList.add(ChildCategoryRowData(i))
//        }
//
//        staples = context.resources.getStringArray(R.array.staples)
//        val staplesList = mutableListOf<ChildCategoryRowData>()
//        for(i in staples){
//            staplesList.add(ChildCategoryRowData(i))
//        }
//        fruitsVeggies = context.resources.getStringArray(R.array.fruits_veggie)
//        val fruitsList = mutableListOf<ChildCategoryRowData>()
//        for(i in fruitsVeggies){
//            fruitsList.add(ChildCategoryRowData(i))
//        }
//        beverages = context.resources.getStringArray(R.array.beverages)
//        val beveragesList = mutableListOf<ChildCategoryRowData>()
//        for(i in beverages){
//            beveragesList.add(ChildCategoryRowData(i))
//        }
//        snacks = context.resources.getStringArray(R.array.snacks_packed_foods)
//        val snacksList = mutableListOf<ChildCategoryRowData>()
//        for(i in snacks){
//            snacksList.add(ChildCategoryRowData(i))
//        }
//
//        beauty = context.resources.getStringArray(R.array.beauty_and_hygiene)
//        val beautyList = mutableListOf<ChildCategoryRowData>()
//        for(i in beauty){
//            beautyList.add(ChildCategoryRowData(i))
//        }
//        household = context.resources.getStringArray(R.array.cleaning_and_household)
//        val householdsList = mutableListOf<ChildCategoryRowData>()
//        for(i in household){
//            householdsList.add(ChildCategoryRowData(i))
//        }
//        kitchen = context.resources.getStringArray(R.array.kitchen_garden_pets)
//        val kitchenList = mutableListOf<ChildCategoryRowData>()
//        for(i in kitchen){
//            kitchenList.add(ChildCategoryRowData(i))
//        }
//        meat = context.resources.getStringArray(R.array.meat_and_eggs)
//        val meatList = mutableListOf<ChildCategoryRowData>()
//        for(i in meat){
//            meatList.add(ChildCategoryRowData(i))
//        }
//        childCategoryArray = mutableListOf(
//            dairyList,
//            meatList,
//            staplesList,
//            fruitsList,
//            beveragesList,
//            snacksList,
//            beautyList,
//            householdsList,
//            kitchenList
//        )


    }


}