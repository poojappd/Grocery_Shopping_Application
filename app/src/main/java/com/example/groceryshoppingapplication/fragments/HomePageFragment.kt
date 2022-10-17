package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.os.Bundle
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
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.listeners.CategoryItemTouchListener
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_addresses.view.*
import kotlinx.android.synthetic.main.fragment_home_page.view.*


class HomePageFragment : Fragment() {
    val viewmodel : UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
//        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
//        actionBar.elevation = 0f
//        actionBar.setTitle("Hello, ${viewmodel.currentUser.value?.firstName}")

        requireActivity().findViewById<BottomNavigationView>(com.example.groceryshoppingapplication.R.id.bottomNavigationView).visibility = View.VISIBLE
        val view =  inflater.inflate(com.example.groceryshoppingapplication.R.layout.fragment_home_page, container, false)
        val recyclerView = view.homePageFragment_category_rv
        val adViewPager = view.top_advertisement_viewPager
        adViewPager.adapter = AdvertisementViewPager()
        recyclerView.setNestedScrollingEnabled(false)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2);
        recyclerView.adapter = HomePageAdapterCategories(CategoryItemTouchListenerImpl())
            return view
        }

    inner class CategoryItemTouchListenerImpl(): CategoryItemTouchListener {
        override fun navigateToFragment(subCategory: SubCategory) {
            val action = AllCategoriesFragmentDirections.actionAllCategoriesFragmentToProductsListFragment(subCategory)
            findNavController().navigate(action)
        }

        override fun getContext(): Context {
            return requireContext()
        }

        fun navigate(){
            findNavController().navigate(R.id.productsListFragment)
        }

    }

}


