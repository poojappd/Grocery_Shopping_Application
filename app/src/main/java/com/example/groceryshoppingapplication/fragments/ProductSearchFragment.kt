package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.groceryshoppingapplication.ProductSearchResultsFragment
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.RecentSearchAdapter
import com.example.groceryshoppingapplication.adapters.TrendingSearchAdapter
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.viewmodels.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_product_search.view.*
import kotlinx.android.synthetic.main.fragment_product_search_results.view.*


class ProductSearchFragment : Fragment() {

    private lateinit var childFragmentContainer: FragmentContainerView
    private lateinit var searchView: SearchView
    private lateinit var recentSearchContainer: ViewGroup
    private var lastSuggestionStackId: Int? = null
    private var lastQuery: String? = null
    private var lastSubmittedQuery: String? = null
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val searchSuggestionViewModel: SearchSuggestionViewModel by activityViewModels {
        SearchSuggestionViewModelFactory(requireActivity().applicationContext)
    }

    private val args: ProductSearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_search, container, false)
        Log.e(
            TAG,
            "  back stack count search frag------>" + childFragmentManager.backStackEntryCount.toString()
        )
        recentSearchContainer = view.recentSearch_Container
        val recyclerView = view.recyclerView
        val trendingSearchRecyclerView = view.trending_search_recyclerView
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        trendingSearchRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        userViewModel.recentSearches.observe(viewLifecycleOwner) {
            recyclerView.adapter =
                RecentSearchAdapter(it) { query: String ->
                    searchView.setQuery(query, true)
                }
        }
        trendingSearchRecyclerView.adapter = TrendingSearchAdapter(listOf("Pineapple","Pepsi", "Coke", "Ice cream", "Maggi", "masala")){ query: String ->
            searchView.setQuery(query, true)}

        searchView = view.searchView
//        if (args.isSearchViewFocused)
//            searchView.requestFocus()

        searchView.setOnQueryTextListener(SearchQueryListener())
        searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn).setOnClickListener {
            searchView.setQuery("", true)
            searchView.clearFocus()
        }
        searchView.setOnQueryTextFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            requireActivity().bottomNavigationView.isVisible = !hasFocus
        })
        childFragmentContainer = view.search_Fragment
        childFragmentManager.addOnBackStackChangedListener {
            val backStackCount = childFragmentManager.backStackEntryCount
            Log.e(TAG, "BACK STACK CHANGED" + " $backStackCount")
            recentSearchContainer.isVisible = backStackCount==0


        }

        return view
    }

    private fun showInputMethod(view: View) {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    fun hideInputMethod() {
        val view = searchView
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken,0)
    }

    private fun getProductCodesFromProduct(groceryItems:List<GroceryItemEntity>): IntArray {
        val productCodes = mutableListOf<Int>()
        groceryItems.forEach {item->
            productCodes.add(item.productCode)
        }
        return productCodes.toIntArray()
    }

    private inner class SearchQueryListener : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            Log.e(TAG, "onquerySumbit $query")

            if (query != null// && (query != lastSubmittedQuery)
            ) {
                searchProductInInventory(query)
                hideInputMethod()
                lastSubmittedQuery = query
                userViewModel.addToRecentSearch(query)
                return true
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Log.e(TAG, "QueryChange - *$newText*")

            if (newText != null) {

                if (newText == "") {
                popLastChildBackStackEntry()
                    Log.e(TAG, "EMpty Queryyyy-----------")
                } else {
                    if (newText != lastQuery) {
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
            inventoryViewModel.searchProducts(searchQuery).observe(viewLifecycleOwner) {
                if (it.size > 0) {

                    val matchingCategories = mutableListOf<String>()
                    val titles = mutableListOf<String>()
                    val categoriesEnum = mutableListOf<Enum<*>>()
                    val productCodes = mutableListOf<Int>()

                    GeneralCategory.values().forEach {
                        val upperCasedValue = it.value.uppercase()
                        if (upperCasedValue.contains(searchQuery.uppercase())) {
                            matchingCategories.add(it.value)
                            categoriesEnum.add(it)
                        }
                    }
                    it?.forEach { it2 ->
                        Log.e(TAG,"${it2.brandName} ${it2.itemName.uppercase()}")

                        if(titles.size<=7) {
                            if (
                                it2.brandName.uppercase().split(" ").any { string ->
                                    Log.e(TAG, string)
                                    (searchQuery.uppercase().split(" ").any { splitQuery ->
                                        Log.e(TAG, splitQuery)
                                        string.startsWith(splitQuery)
                                    })
                                }
                            ) {
                                titles.add(it2.brandName + " " + it2.itemName)
                                productCodes.add(it2.productCode)
                                val category = it2.subCategory.value
                                if (!matchingCategories.contains(category)) {
                                    matchingCategories.add(category)
                                    categoriesEnum.add(it2.subCategory)
                                }
                            } else if (
                                it2.itemName.uppercase().split(" ").any { string ->
                                    (searchQuery.uppercase().split(" ")
                                        .any { splitQuery -> string.startsWith(splitQuery) })
                                }

                            ) {
                                titles.add(it2.brandName + " " + it2.itemName)
                                productCodes.add(it2.productCode)
                                val category = it2.subCategory.value
                                if (!matchingCategories.contains(category)) {
                                    matchingCategories.add(category)
                                    categoriesEnum.add(it2.subCategory)
                                }
                            }
                        }
                    }


                    childFragmentManager.beginTransaction().apply {
                        add(R.id.search_Fragment, ProductSearchResultsFragment.newInstance(productCodes.toIntArray()))
                        addToBackStack("searchresultProduct")
                        lastSuggestionStackId = commit()
                    }
                }

            }
        }


        private fun displaySuggestions(searchQuery: String) {
            if(childFragmentManager.backStackEntryCount==0) {
                childFragmentManager.beginTransaction().apply {
                    add(R.id.search_Fragment, SearchSuggestionsFragment.newInstance(searchQuery))
                    addToBackStack("sugg")
                    lastSuggestionStackId = commit()
                }
            }
            searchSuggestionViewModel.setSuggestion(searchQuery)
            searchSuggestionViewModel.searchQuery.value = searchQuery
        }

    }


    fun searchGeneralCategoryInInventory(generalCategory: GeneralCategory) {
        val items = inventoryViewModel.getProductsUnderGeneralCategory(generalCategory)
        popLastChildBackStackEntry()

        if (items.size > 0) {
            childFragmentManager.apply {
                beginTransaction().apply {

                    add(R.id.search_Fragment, ProductSearchResultsFragment.newInstance(getProductCodesFromProduct(items)))
                    addToBackStack("searchresultProduct")
                    lastSuggestionStackId = commit()
                }
            }
        }
    }

    fun searchSubCategoryInInventory(subCategory: SubCategory) {

        val items = inventoryViewModel.getProductsUnderSubCategory(subCategory)
        popLastChildBackStackEntry()
        if (items.size > 0) {
            childFragmentManager.apply {
                beginTransaction().apply {
                    add(R.id.search_Fragment, ProductSearchResultsFragment.newInstance(getProductCodesFromProduct(items)))
                    addToBackStack("searchresultProduct")
                    lastSuggestionStackId = commit()
                }
            }
        }
    }

    private fun popLastChildBackStackEntry() {
        lastSuggestionStackId?.let {
            childFragmentManager.popBackStackImmediate(it, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun setSearchViewQuery(searchQuery: String) {
        searchView.setQuery(searchQuery, false)
        lastQuery = searchQuery
        searchView.clearFocus()
    }

}