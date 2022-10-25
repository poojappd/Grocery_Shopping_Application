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
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*
import kotlinx.android.synthetic.main.fragment_products_list.view.*


class ProductsListFragment : Fragment() {
    private lateinit var productsInCategoriesAdapter: ProductsInCategoriesAdapter
    private val args: ProductsListFragmentArgs by navArgs()
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
        val view = inflater.inflate(R.layout.fragment_products_list, container, false)
        view.toolbar_productList.setNavigationOnClickListener { requireActivity().onBackPressed() }
        val recyclerView = view.products_list_recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        if (args.subCategory != SubCategory.NONE) {
            val subCategory = args.subCategory
            view.toolbar_productList.title = subCategory.value
            (requireActivity() as AppCompatActivity).supportActionBar?.title = subCategory.value
            // Inflate the layout for this fragment

            val items = inventoryViewModel.getProductsUnderSubCategory(subCategory)
            productsInCategoriesAdapter =
                ProductsInCategoriesAdapter(items, requireContext(), ProductListTouchListenerImpl())
            recyclerView.adapter = productsInCategoriesAdapter
        }
        else{
            val category = args.generalCategory
            view.toolbar_productList.title = category.value
             (requireActivity() as AppCompatActivity).supportActionBar?.title = category.value
            // Inflate the layout for this fragment

            val items = inventoryViewModel.getProductsUnderGeneralCategory(category)
            productsInCategoriesAdapter =
                ProductsInCategoriesAdapter(items, requireContext(), ProductListTouchListenerImpl())
            recyclerView.adapter = productsInCategoriesAdapter
        }

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