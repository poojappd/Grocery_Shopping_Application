package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.adapters.ParentCategoryAdapter
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.CategoriesUtil


class AllCategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_all_categories, container, false)

        val recyclerViewParent = view.findViewById<RecyclerView>(R.id.category_list)
        recyclerViewParent.layoutManager = LinearLayoutManager(context)
        val generalCategories = CategoriesUtil(requireContext())

        recyclerViewParent.adapter = ParentCategoryAdapter(
            findNavController(),
            requireContext(),
            generalCategories.parentCategoryArray,
            generalCategories.childCategoryArray
        )

        return view
    }


}