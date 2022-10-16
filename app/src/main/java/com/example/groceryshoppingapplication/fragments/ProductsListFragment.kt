package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.listeners.ProductListTouchListener
import com.example.groceryshoppingapplication.adapters.ProductsInCategoriesAdapter
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_products_list.view.*


class ProductsListFragment : Fragment() {
    private lateinit var productsInCategoriesAdapter: ProductsInCategoriesAdapter
    private val args : ProductsListFragmentArgs by navArgs()
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
            InventoryViewModelFactory(requireActivity().applicationContext)
    }
    private val viewmodel : UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val subCategory = args.subCategory
        (requireActivity() as AppCompatActivity).supportActionBar?.title = subCategory.value
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_products_list, container, false)
        val recyclerView = view.products_list_recyclerView
         recyclerView.layoutManager = LinearLayoutManager(context)
        val items = inventoryViewModel.getProductsUnderSubCategory(subCategory)
        productsInCategoriesAdapter =
            ProductsInCategoriesAdapter(items, requireContext(), ProductListTouchListenerImpl())
        recyclerView.adapter = productsInCategoriesAdapter

//
//        inventoryViewModel.allProductsInInventory.observe(viewLifecycleOwner){
//            Log.e(TAG, "INSIDE VIEWMODEL OBSERVE $it")
//            productsInCategoriesAdapter = ProductsInCategoriesAdapter(it,requireContext(),ProductListTouchListenerImpl())
//            recyclerView.adapter = productsInCategoriesAdapter
//        }
        return view
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
                ProductsListFragmentDirections.actionProductsListFragmentToSingleProductViewFragment(
                    productCode
                )
            findNavController().navigate(action)
        }

    }


}