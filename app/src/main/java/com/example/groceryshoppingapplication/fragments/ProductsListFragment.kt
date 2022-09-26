package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.ProductsInCategoriesAdapter
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.SharedViewModel
import com.example.groceryshoppingapplication.SharedViewModelFactory
import kotlinx.android.synthetic.main.fragment_products_list.view.*


class ProductsListFragment : Fragment() {
    private lateinit var productsInCategoriesAdapter: ProductsInCategoriesAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels {
            SharedViewModelFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_products_list, container, false)
        val recyclerView = view.products_list_recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        sharedViewModel.allProductsInInventory.observe(viewLifecycleOwner){
            Log.e(TAG, "INSIDE VIEWMODEL OBSERVE $it")
            productsInCategoriesAdapter = ProductsInCategoriesAdapter(it)
            recyclerView.adapter = productsInCategoriesAdapter
        }
        return view
    }


}