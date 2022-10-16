package com.example.groceryshoppingapplication.enums

enum class GeneralCategory(val value:String) {
    FRUITS_AND_VEGETABLES("Fruits & Vegetables"),
    DAIRY_AND_BAKERY("Bakery, Cakes & Dairy"),
    BEVERAGES("Beverages"),
    STAPLES("Staples"),
    MEAT_AND_EGGS("Meat & Eggs"),
    BEAUTY_AND_HYGIENE("Beauty & Hygiene"),
    SNACKS_AND_BRANDED_FOODS("Snacks & Branded Foods"),
    CLEANING_AND_HOUSEHOLD("Cleaning & Household"),
    GARDEN_AND_PETS("Garden & Pets"),
}

val categoryMap:Map<GeneralCategory,List<SubCategory>> =
    mapOf(
        GeneralCategory.FRUITS_AND_VEGETABLES to listOf(SubCategory.HERBS_AND_SEASONINGS,SubCategory.FRESH_FRUITS, SubCategory.FRESH_VEGETABLES, SubCategory.CUTS_AND_SPROUTS, SubCategory.EXOTIC_FRUITS_AND_VEGGIES, SubCategory.FLOWER_BOUQUETS_BUNCHES),
        GeneralCategory.DAIRY_AND_BAKERY to listOf(SubCategory.DAIRY,SubCategory.BREADS_AND_BUNS, SubCategory.RUSK,SubCategory.ICE_CREAMS_AND_DESSERTS,SubCategory.CAKES_AND_PASTRIES) ,
        GeneralCategory.BEVERAGES to listOf(SubCategory.ENERGY_AND_SOFT_DRINKS, SubCategory.HEALTH_DRINK_SUPPLEMENT,SubCategory.TEA,SubCategory.COFFEE,SubCategory.FRUIT_JUICES_AND_DRINKS,SubCategory.SYRUPS_POWDERS_AND_MIXES),
        GeneralCategory.STAPLES to listOf(SubCategory.ATTA_FLOURS_AND_SOOJI,SubCategory.RICE_AND_RICE_PRODUCTS, SubCategory.DALS_AND_PULSES,SubCategory.EDIBLE_OILS_AND_GHEE,SubCategory.SALT_SUGAR_AND_JAGGERY,SubCategory.MASALAS_AND_SPICES,SubCategory.DRY_FRUITS),
        GeneralCategory.SNACKS_AND_BRANDED_FOODS to listOf(SubCategory.NOODLES_PASTA_VERMICELLI, SubCategory.BREAKFAST_CEREALS, SubCategory.FROZEN_VEGGIES_AND_SNACKS, SubCategory.BISCUITS_AND_COOKIES, SubCategory.CHIPS_AND_CORN_SNACKS, SubCategory.SPREADS_SAUCES_KETCHUP, SubCategory.CHOCOLATES_AND_CANDIES, SubCategory.PICKLES_AND_CHUTNEY),
        GeneralCategory.BEAUTY_AND_HYGIENE to listOf(SubCategory.FEMININE_HYGIENE, SubCategory.HEALTH_AND_MEDICINE,SubCategory.HAIR_CARE,SubCategory.SKIN_CARE, SubCategory.MAKEUP, SubCategory.FRAGRANCES_AND_DEOS,SubCategory.ORAL_CARE,SubCategory.BATH_AND_HAND_WASH),
        GeneralCategory.CLEANING_AND_HOUSEHOLD to listOf(SubCategory.DETERGENTS_AND_DISHWASH, SubCategory.FRESHENERS_AND_REPELLENTS,SubCategory.MOPS_BRUSHES_AND_SCRUBS, SubCategory.ALL_PURPOSE_CLEANERS,SubCategory.STATIONERY,SubCategory.KITCHEN_ACCESSORIES),
        GeneralCategory.GARDEN_AND_PETS to listOf(SubCategory.GARDENING_TOOLS,SubCategory.SEEDS, SubCategory.DOG_FOOD, SubCategory.CAT_FOOD,SubCategory.OTHER_PET_FOOD, SubCategory.PET_ACCESSORIES),
        GeneralCategory.MEAT_AND_EGGS to listOf(SubCategory.EGGS, SubCategory.MUTTON_AND_LAMB, SubCategory.FISH_AND_SEAFOOD, SubCategory.CHICKEN_AND_OTHER_MEATS, SubCategory.SAUSAGES_BACON_AND_SALAMI )
    )
