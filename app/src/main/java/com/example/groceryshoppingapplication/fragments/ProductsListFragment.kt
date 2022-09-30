package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.ProductsInCategoriesAdapter
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.InventoryViewModel
import com.example.groceryshoppingapplication.InventoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_products_list.view.*


class ProductsListFragment : Fragment() {
    private lateinit var productsInCategoriesAdapter: ProductsInCategoriesAdapter
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
            InventoryViewModelFactory(requireActivity().applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_products_list, container, false)
        val recyclerView = view.products_list_recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        inventoryViewModel.allProductsInInventory.observe(viewLifecycleOwner){
            Log.e(TAG, "INSIDE VIEWMODEL OBSERVE $it")
            productsInCategoriesAdapter = ProductsInCategoriesAdapter(it,requireContext(),findNavController())
            recyclerView.adapter = productsInCategoriesAdapter
        }
        return view
    }


}