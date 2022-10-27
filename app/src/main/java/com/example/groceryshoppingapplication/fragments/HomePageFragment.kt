package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.AdvertisementViewPager
import com.example.groceryshoppingapplication.adapters.HomePageAdapterCategories
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.listeners.CategoryItemTouchListener
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_addresses.view.*
import kotlinx.android.synthetic.main.fragment_home_page.view.*
import kotlinx.android.synthetic.main.fragment_home_page.view.searchView
import kotlinx.android.synthetic.main.fragment_product_search.view.*


class HomePageFragment : Fragment() {
    val viewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        findNavController().backQueue.forEach {
            Log.e(ContentValues.TAG,"   ----    "+ it.destination.toString())

        }

        // Inflate the layout for this fragment
//        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
//        actionBar.elevation = 0f

        requireActivity().findViewById<BottomNavigationView>(com.example.groceryshoppingapplication.R.id.bottomNavigationView).visibility =
            View.VISIBLE
        val view = inflater.inflate(R.layout.fragment_home_page, container,
            false
        )
        view.materialTextView24.text = "Hello, ${viewmodel.currentUser.value?.firstName}"

        val recyclerView = view.homePageFragment_category_rv
        val adViewPager = view.top_advertisement_viewPager
        adViewPager.adapter = AdvertisementViewPager()
        recyclerView.setNestedScrollingEnabled(false)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2);
        recyclerView.adapter = HomePageAdapterCategories(CategoryItemTouchListenerImpl())

        view.searchView.setOnClickListener {
            findNavController().navigate(R.id.productSearchFragment)
        }
        return view
    }

    inner class CategoryItemTouchListenerImpl() {

        fun getContext(): Context {
            return requireContext()
        }

        fun navigateToProductsByGeneralCategory(generalCategory: GeneralCategory) {
            val action = HomePageFragmentDirections.actionHomePageFragmentToProductsListFragment(
                generalCategory = generalCategory
            )
            findNavController().navigate(action)
        }
    }

}




