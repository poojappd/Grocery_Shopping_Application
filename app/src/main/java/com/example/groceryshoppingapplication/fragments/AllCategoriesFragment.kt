package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.groceryshoppingapplication.adapters.ParentCategoryAdapter
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.CategoriesUtil
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.listeners.CategoryItemTouchListener
import kotlinx.android.synthetic.main.category_single_row.view.*
import kotlinx.android.synthetic.main.fragment_all_categories.view.*


class AllCategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_all_categories, container, false)
        val recyclerViewParent = view.findViewById<RecyclerView>(R.id.category_list)

        recyclerViewParent.layoutManager = LinearLayoutManager(context)
        val generalCategories = CategoriesUtil(requireContext())
        val parentAndChildCategory = generalCategories.categoryMap
        val parentImages = generalCategories.categoryImages
        recyclerViewParent.adapter = ParentCategoryAdapter(
            parentAndChildCategory,
            parentImages,
            false,
            CategoryItemTouchListenerImpl()
        )
        view.expand_collapse_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            TransitionManager.beginDelayedTransition(recyclerViewParent)
           // recyclerViewParent.adapter?.notifyDataSetChanged()
            if (isChecked) {
                recyclerViewParent.adapter = ParentCategoryAdapter(
                    parentAndChildCategory,
                    parentImages,
                    true,
                    CategoryItemTouchListenerImpl()
                )
                view.Expand_Collapse_TV.text = "Collapse all"
            }
            else {
                recyclerViewParent.adapter = ParentCategoryAdapter(
                    parentAndChildCategory,
                    parentImages,
                    false,
                    CategoryItemTouchListenerImpl()
                )
                view.Expand_Collapse_TV.text = "Expand all"


            }
        }


        return view
    }

    private inner class CategoryItemTouchListenerImpl(): CategoryItemTouchListener{

        override fun navigateToFragment(subCategory: SubCategory) {
            val action = AllCategoriesFragmentDirections.actionAllCategoriesFragmentToProductsListFragment(subCategory)
            findNavController().navigate(action)
        }

        override fun getContext(): Context {
            return requireContext()
        }


    }




}