package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.*
import com.example.groceryshoppingapplication.ProductSearchResultsFragment
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_product_search.view.*
import kotlinx.android.synthetic.main.fragment_product_search_results.view.*


class ProductSearchFragment : Fragment() {

    private lateinit var childFragmentContainer: FragmentContainerView
    private lateinit var searchView:SearchView
    private var lastSuggestionStackId:Int? = null
    private var lastQuery:String? = null
    private var lastSubmittedQuery:String? = null
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_search, container, false)
        Log.e(TAG, "  back stack count search frag------>"+childFragmentManager.backStackEntryCount.toString())
        searchView = view.searchView
        searchView.requestFocus()
        searchView.setOnQueryTextListener(SearchQueryListener())
        searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn).setOnClickListener{
            searchView.setQuery("",true)
            searchView.clearFocus()
        }
        childFragmentContainer = view.search_Fragment
        childFragmentManager.addOnBackStackChangedListener {
            val backStackCount = childFragmentManager.backStackEntryCount
            Log.e(TAG,"BACK STACK CHANGED"+" $backStackCount")

        }

        return view
    }

    private inner class SearchQueryListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            //childFragmentManager.popBackStackImmediate()
            if (query != null && query!= lastSubmittedQuery) {
                searchProductInInventory(query)
                lastSubmittedQuery = query
                return true
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Log.e(TAG,"Query - *$newText*")

            if (newText != null) {
                if(newText =="") {
                    childFragmentContainer.visibility = View.GONE
                    Log.e(TAG, "EMpty Queryyyy-----------")
                }else{
                    if(newText!=lastQuery) {
                        childFragmentContainer.visibility = View.VISIBLE

                        displaySuggestions(newText)
                        lastQuery = newText
                    }
                }
                return true
            }
            return false
        }


        fun searchProductInInventory(searchQuery: String) {
            popLastChildBackStackEntry()
            inventoryViewModel.searchProducts("%$searchQuery%").observe(viewLifecycleOwner) {
                if(it.size>0) {
                    childFragmentManager.beginTransaction().apply {
                        add(R.id.search_Fragment, ProductSearchResultsFragment(it))
                        addToBackStack("searchResult")
                        lastSuggestionStackId = commit()
                    }
                }

            }
        }



        fun displaySuggestions(searchQuery: String) {
            popLastChildBackStackEntry()
                childFragmentManager.beginTransaction().apply {
                    add(R.id.search_Fragment, SearchSuggestionsFragment(searchQuery))
                    addToBackStack("sugg")
                    lastSuggestionStackId = commit()

                }

        }

    }



    fun searchGeneralCategoryInInventory(generalCategory: GeneralCategory){
     val items = inventoryViewModel.getProductsUnderGeneralCategory(generalCategory)
        popLastChildBackStackEntry()
        if(items.size>0) {
            childFragmentManager.apply {
                beginTransaction().apply {
                    add(R.id.search_Fragment, ProductSearchResultsFragment(items))
                    addToBackStack("searchCategRes")
                    lastSuggestionStackId = commit()
                }
            }
        }
    }
    fun searchSubCategoryInInventory(subCategory: SubCategory){

        val items = inventoryViewModel.getProductsUnderSubCategory(subCategory)
      popLastChildBackStackEntry()
        if(items.size>0) {
            childFragmentManager.apply {
                beginTransaction().apply {
                    add(R.id.search_Fragment, ProductSearchResultsFragment(items))
                    addToBackStack("searchSubCategRes")
                    lastSuggestionStackId = commit()
                }
            }
        }
    }
private fun popLastChildBackStackEntry(){
    lastSuggestionStackId?.let {
        childFragmentManager.popBackStackImmediate(it,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
fun setSearchViewQuery(searchQuery: String){
    searchView.setQuery(searchQuery,false)
    lastQuery = searchQuery
    searchView.clearFocus()
}

}