package com.example.groceryshoppingapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.Utils.TaskAssigner
import com.example.groceryshoppingapplication.adapters.ProductsInCategoriesAdapter
import com.example.groceryshoppingapplication.fragments.ProductRefineFragment
import com.example.groceryshoppingapplication.fragments.ProductSearchFragmentDirections
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.viewmodels.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_product_search_results.*
import kotlinx.android.synthetic.main.fragment_product_search_results.view.*
import java.io.Serializable
import java.util.*


class ProductSearchResultsFragment : Fragment(), Serializable {

    private lateinit var filterButton:View
    private lateinit var recyclerView: RecyclerView
    private lateinit var nothingFound_tv:TextView
    private val filterViewModel:FilterViewModel by activityViewModels {
        FilterViewModelFactory()
    }
    val filterListener = SetFilter()
    private val inventoryViewModel:InventoryViewModel by viewModels {
        InventoryViewModelFactory(requireContext())
    }
    private lateinit var items:List<GroceryItemEntity>
    private val viewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val modifyOrderViewModel:ModifyOrderViewModel by activityViewModels {
        ModifyOrderViewModelFactory(requireContext().applicationContext)
    }
    companion object {
        fun newInstance(productCodes: IntArray): ProductSearchResultsFragment {
            val fragment = ProductSearchResultsFragment()
            val bundle = Bundle()
            bundle.putIntArray("productCodes", productCodes)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.e(ContentValues.TAG, "  back stack count results frag------>"+parentFragmentManager.backStackEntryCount.toString())
        val view = inflater.inflate(R.layout.fragment_product_search_results, container, false)
        val mutableItems = mutableListOf<GroceryItemEntity>()
        val productCodes = arguments?.getIntArray("productCodes")!!
        productCodes.forEach {
            Log.e(TAG, "got from bundle: ${it}")
        }
        for (i in productCodes){
            mutableItems.add(inventoryViewModel.getProductDetailsSynchronously(i))
        }
        items = mutableItems
        filterViewModel.setInitialSearchResults(items)
        recyclerView = view.product_search_result_RV
        nothingFound_tv = view.nothingFound_tv
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        filterViewModel.getRefinedData()?.let {
            Log.e(TAG,"Refined data exists")
            recyclerView.adapter =
                ProductsInCategoriesAdapter(it, requireContext(), ProductListTouchListenerImpl(viewmodel,inventoryViewModel,modifyOrderViewModel))
        } ?: run {
            Log.e(TAG,"Refined data doesn't exist passing ${items.toString()}")
            recyclerView.adapter =
                ProductsInCategoriesAdapter(items, requireContext(), ProductListTouchListenerImpl(viewmodel,inventoryViewModel,modifyOrderViewModel))
        }

        filterButton = view.filterButton
        filterViewModel.filterCount.observe(viewLifecycleOwner){
            if(it>0) {
                filterBadge.visibility = View.VISIBLE
                filterBadge.text = it.toString()
            }
            else{
                filterBadge.visibility = View.GONE
            }
        }
        filterButton.setOnClickListener {
            val filterDialog = ProductRefineFragment()
            filterDialog.show(childFragmentManager,"refine")
        }
        return view
    }

    private inner class ProductListTouchListenerImpl(
        override val userViewModelChild: UserViewModel,
        override val inventoryViewModelChild: InventoryViewModel,
        override var modifyOrderViewModel: ModifyOrderViewModel
    ) : TaskAssigner() {

        override fun navigate(productCode: Int) {
            val action =
                ProductSearchFragmentDirections.actionProductSearchFragmentToSingleProductViewFragment(
                    productCode
                )

            findNavController().navigate(action)
        }

    }

    inner class SetFilter :Serializable{
        fun applyFilter(){
            toggleSearchResultVisibility(false)
            val newItems = items.toMutableList()
            var brandFilteredItems = mutableListOf<GroceryItemEntity>()
            var categoryFilteredItems = mutableListOf<GroceryItemEntity>()
            var sizeFilteredItems = mutableListOf<GroceryItemEntity>()

            filterViewModel.temporaryFilterConfiguration?.let {
                filterViewModel.appliedFilterConfiguration = it
            }
            filterViewModel.appliedFilterConfiguration?.let {
                Log.e(TAG,it.getBrandFilters().toString() +"\n")
                val brandFilter = it.getBrandFilters()
                val sizeFilter = it.getSelectedSizeFilters()
                val categFilter = it.getSelectedCategoryFilters()
                for(item in newItems) {
                    if(brandFilter.size>0){
                        if(brandFilter.contains(item.brandName))
                            brandFilteredItems.add(item)
                    }
                    else {
                        brandFilteredItems = newItems
                        break
                    }
                }

                for(item in brandFilteredItems){
                    if(categFilter.size>0){
                        if(categFilter.contains(item.subCategory))
                            categoryFilteredItems.add(item)
                    }
                    else {
                        categoryFilteredItems =  brandFilteredItems
                        break
                    }
                }

                for(item in categoryFilteredItems){
                    if(sizeFilter.size>0){
                        if(sizeFilter.contains(item.capacity to item.capacityUnit))
                            sizeFilteredItems.add(item)
                    }
                    else {
                        sizeFilteredItems = categoryFilteredItems
                        break
                    }
                }

                 if(sizeFilteredItems.size>0){
                     it.temporarySortPriceLowToHigh?.let {sortAscending->
                         Collections.sort(sizeFilteredItems, Comparator { o1: GroceryItemEntity, o2: GroceryItemEntity ->
                             return@Comparator o1.unitPrice.compareTo(o2.unitPrice)
                         })
                         if(!sortAscending)
                             sizeFilteredItems.reverse()
                     }
                     recyclerView.adapter =
                         ProductsInCategoriesAdapter(sizeFilteredItems, requireContext(), ProductListTouchListenerImpl(viewmodel, inventoryViewModel, modifyOrderViewModel))
                     filterViewModel.fixAsFinalConfiguration(sizeFilteredItems)
                }
                else{
                   toggleSearchResultVisibility(true)
                 }

            }

        }
        fun clearFilter(){
            toggleSearchResultVisibility(false)

            recyclerView.adapter =
                ProductsInCategoriesAdapter(items, requireContext(), ProductListTouchListenerImpl(viewmodel, inventoryViewModel, modifyOrderViewModel))
            filterViewModel.appliedFilterConfiguration = null
            filterViewModel.clearAllSavedFilter()
            filterViewModel.clearTemporaryConfiguration()
        }
        private fun toggleSearchResultVisibility(noResults:Boolean){
            if(noResults) {
                recyclerView.visibility = View.GONE
                nothingFound_tv.visibility = View.VISIBLE
            }
            else{
                recyclerView.visibility = View.VISIBLE
                nothingFound_tv.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        Log.e(TAG, "SEARCH RESULT NOT DESTROYED")
        filterViewModel.clearAllSavedFilter()
        super.onDestroy()

    }

}