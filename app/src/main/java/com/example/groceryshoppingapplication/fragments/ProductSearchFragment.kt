package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.ProductsInCategoriesAdapter
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.listeners.ProductListTouchListener
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_product_search.view.*

class ProductSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    private val viewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_search, container, false)
        val searchView = view.searchView
        searchView.requestFocus()


        searchView.setOnQueryTextListener(SearchQueryListener())
        recyclerView = view.search_recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    private inner class SearchQueryListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                searchInventory("$query%")
                return true
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
                searchInventory("$newText%")
                return true
            }
            else if(newText ==""){
                recyclerView.visibility = View.GONE
            }
            return false
        }

        fun searchInventory(searchQuery: String) {
            inventoryViewModel.searchProducts(searchQuery).observe(viewLifecycleOwner) {
                recyclerView.adapter = ProductsInCategoriesAdapter(
                    it,
                    requireContext(),
                    ProductListTouchListenerImpl()
                )
            }
        }

    }

    private inner class ProductListTouchListenerImpl : ProductListTouchListener {
        override fun addToCart(productCode: Int) {
            viewmodel.addToCart(productCode)
        }

        override fun removeFromCart(productCode: Int) {
            viewmodel.removeItem(productCode)
        }

        override fun checkItemInCart(productCode: Int): Response {
            return viewmodel.checkItemInCart(productCode)
        }

        override fun navigate(productCode: Int) {
            val action =
                ProductSearchFragmentDirections.actionProductSearchFragmentToSingleProductViewFragment(productCode)

            findNavController().navigate(action)
        }

    }


}