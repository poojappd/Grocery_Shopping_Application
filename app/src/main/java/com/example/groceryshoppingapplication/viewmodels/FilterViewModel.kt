package com.example.groceryshoppingapplication.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.enums.MeasuringUnit
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.models.GroceryItemEntity

class FilterViewModel : ViewModel() {

    private lateinit var originalResults: LiveData<List<GroceryItemEntity>>
    val brands = mutableListOf<String>()
    val packSizes = mutableListOf<String>()
    val packSizesEnums = mutableListOf<Pair<Double, MeasuringUnit>>()
    val categories = mutableListOf<SubCategory>()
    val categoryString = mutableListOf<String>()
    var temporaryFilterConfiguration: FilterConfiguration? = null
    var appliedFilterConfiguration: FilterConfiguration? = null
    private var filterAppliedData: List<GroceryItemEntity>? = null
    val temporaryFilterConfigurationCleared: Boolean
        get() = _temporaryFilterConfigurationCleared
    private var _temporaryFilterConfigurationCleared: Boolean = false
    val filterCount:LiveData<Int>
    get() = _filterCount
    private var _filterCount = MutableLiveData<Int>(0)
    val sizeFilterCount:Int
        get() = _sizeFilterCount
    private var _sizeFilterCount = 0
    val brandFilterCount
        get() = _brandFilterCount
    private var _brandFilterCount = 0
    val categFilterCount
        get() = _categFilterCount
    private var _categFilterCount = 0


    fun setInitialSearchResults(results: List<GroceryItemEntity>) {
        originalResults = MutableLiveData(results)
        updateOriginalResults()
    }

    fun fixAsFinalConfiguration(appliedData: List<GroceryItemEntity>) {
        appliedFilterConfiguration = temporaryFilterConfiguration
        appliedFilterConfiguration?.let {
            val sortCount = it.temporarySortPriceLowToHigh?.let { 1 }?:0
            _filterCount.value = (if(it.getBrandFilters().size >0) 1 else 0) + (if(it.getSelectedSizeFilters().size >0) 1 else 0)+ (if(it.getSelectedCategoryFilters().size >0) 1 else 0) +sortCount
            _brandFilterCount = it.getBrandFilters().size
            _categFilterCount = it.getSelectedCategoryFilters().size
            _sizeFilterCount = it.getSelectedSizeFilters().size
            Log.e(TAG, "$_brandFilterCount  $_categFilterCount   $_sizeFilterCount")
        }
        clearTemporaryConfiguration()
        filterAppliedData = appliedData
    }

    fun clearTemporaryConfiguration() {
        temporaryFilterConfiguration =
            FilterConfiguration(brands.size, packSizes.size, categories.size)
        _temporaryFilterConfigurationCleared = true
    }

    fun clearAllSavedFilter() {
        temporaryFilterConfiguration = null
        appliedFilterConfiguration = null
        filterAppliedData = null
        _filterCount.value = 0
        _brandFilterCount  = 0
        _sizeFilterCount = 0
        _categFilterCount = 0
    }

    fun getRefinedData() = filterAppliedData

    private fun updateOriginalResults() {
        brands.clear()
        packSizes.clear()
        categories.clear()
        categoryString.clear()
        packSizesEnums.clear()
        originalResults.value?.forEach {
            val brandName = it.brandName
            val packSize = "${it.capacity} ${it.capacityUnit}"
            val category = it.subCategory
            if (!brands.contains(brandName))
                brands.add(brandName)
            if (!packSizes.contains(packSize)) {
                packSizes.add(packSize)
                packSizesEnums.add((it.capacity to it.capacityUnit))
            }
            if (!categories.contains(category)) {
                categories.add(category)
                categoryString.add(category.value)
            }

        }
        temporaryFilterConfiguration =
            FilterConfiguration(brands.size, packSizes.size, categories.size)
    }

    inner class FilterConfiguration(brandSize: Int, packSize: Int, categorySize: Int) {
        var temporarySortPriceLowToHigh: Boolean? = null
        private val selectedBrandFilters = mutableListOf<String>()
        private val selectedSizeFilters = mutableListOf<Pair<Double, MeasuringUnit>>()
        private val selectedCategoryFilters = mutableListOf<SubCategory>()
        private var _packSizePositions = mutableListOf<Int>()
        private var _brandPositions = mutableListOf<Int>()
        private var _categPositions = mutableListOf<Int>()
        val packSizePositions: List<Int>
            get() = _packSizePositions
        val categPositions: List<Int>
            get() = _categPositions
        val brandPositions: List<Int>
            get() = _brandPositions

        fun setPackSize(position: Int, isSelected:Boolean) {
            _temporaryFilterConfigurationCleared = false
            if (!isSelected) {
                selectedSizeFilters.remove(packSizesEnums[position])
                _packSizePositions.remove(position)
            } else {
                selectedSizeFilters.add(packSizesEnums[position])
                _packSizePositions.add(position)
            }
        }

        fun setCategory(position: Int, isSelected:Boolean) {
            _temporaryFilterConfigurationCleared = false

            if (!isSelected) {
                selectedCategoryFilters.remove(categories[position])
                _categPositions.remove(position)
            } else {
                selectedCategoryFilters.add(categories[position])
                _categPositions.add(position)
            }
        }

        fun setBrand(position: Int, isSelected:Boolean) {
            _temporaryFilterConfigurationCleared = false
            Log.e(TAG, selectedBrandFilters.toString())
            if (!isSelected) {
                Log.e(TAG, "BRAND CONTAINED ALREADY")
                selectedBrandFilters.remove(brands[position])
                _brandPositions.remove(position)
            } else {
                selectedBrandFilters.add(brands[position])
                _brandPositions.add(position)
            }
        }

        fun getSelectedSizeFilters() = selectedSizeFilters
        fun getSelectedCategoryFilters() = selectedCategoryFilters
        fun getBrandFilters() = selectedBrandFilters


    }
}

class FilterViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return FilterViewModel(

        ) as T
    }
}