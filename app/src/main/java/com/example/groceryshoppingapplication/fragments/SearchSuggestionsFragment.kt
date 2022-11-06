package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.ProductSearchResultsFragment
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.ProductsInCategoriesAdapter
import com.example.groceryshoppingapplication.adapters.SuggestionsAdapter
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.SearchSuggestionViewModel
import com.example.groceryshoppingapplication.viewmodels.SearchSuggestionViewModelFactory
import kotlinx.android.synthetic.main.fragment_search_suggestions.view.*

class SearchSuggestionsFragment() : Fragment() {
    lateinit var recyclerView: RecyclerView
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    private val searchSuggestionViewModel:SearchSuggestionViewModel by activityViewModels {
        SearchSuggestionViewModelFactory(requireContext().applicationContext)
    }
    companion object {
        fun newInstance(searchQuery: String): SearchSuggestionsFragment {
            val fragment = SearchSuggestionsFragment()
            val bundle = Bundle()
            bundle.putString("searchQuery", searchQuery)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.e(ContentValues.TAG, "  back stack count sugg frag ------>"+childFragmentManager.backStackEntryCount.toString())

        val view = inflater.inflate(R.layout.fragment_search_suggestions, container, false)
        recyclerView = view.search_recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchSuggestionViewModel.searchQuery.observe(viewLifecycleOwner) { searchQuery ->

            inventoryViewModel.searchProducts("%$searchQuery%").observe(viewLifecycleOwner) {
                Log.e(ContentValues.TAG, "  change observed---$searchQuery  ${it?.size}")

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
                    titles.add(it2.brandName + " " + it2.itemName)
                    productCodes.add(it2.productCode)
                    val category = it2.subCategory.value
                    if (!matchingCategories.contains(category)) {
                        matchingCategories.add(category)
                        categoriesEnum.add(it2.subCategory)
                    }
                }
                SubCategory.values().forEach { sub ->
                    val upperCasedValue = sub.value.uppercase()
                    if (upperCasedValue.contains(searchQuery.uppercase())) {
                        categoriesEnum.add(sub)
                        matchingCategories.add(sub.value)
                    }
                }
                recyclerView.adapter = SuggestionsAdapter(
                    titles,
                    matchingCategories
                ) { searchText: String, index: Int, isProduct: Boolean ->

                    if (isProduct) {
                        val action =
                            ProductSearchFragmentDirections.actionProductSearchFragmentToSingleProductViewFragment(
                                productCodes[index]
                            )

                        findNavController().navigate(action)
                    } else {
                        val parentSearchFragment = parentFragment as ProductSearchFragment

                        parentSearchFragment.setSearchViewQuery(searchText)
                        parentSearchFragment.hideInputMethod()
                        val enumValue = categoriesEnum.get(index)
                        Log.e(ContentValues.TAG, enumValue.name)

                        if (enumValue is GeneralCategory) {
                            parentSearchFragment.searchGeneralCategoryInInventory(enumValue as GeneralCategory)
                        } else
                            parentSearchFragment.searchSubCategoryInInventory(enumValue as SubCategory)
                    }
                }
            }
        }
        return view

    }

}


