package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.adapters.HomePageAdapterCategories
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.elevation = 0f
        actionBar.setTitle("Hello, ${viewmodel.currentUser.value?.firstName}")

        requireActivity().findViewById<BottomNavigationView>(com.example.groceryshoppingapplication.R.id.bottomNavigationView).visibility = View.VISIBLE
        val view =  inflater.inflate(com.example.groceryshoppingapplication.R.layout.fragment_home_page, container, false)
        val someButton = view.findViewById<Button>(com.example.groceryshoppingapplication.R.id.goTo)


        val recyclerView = view.homePageFragment_category_rv
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), RecyclerView.HORIZONTAL,false)
        recyclerView.adapter = HomePageAdapterCategories()
        someButton.setOnClickListener {
                findNavController().navigate(com.example.groceryshoppingapplication.R.id.action_homePageFragment_to_allCategoriesFragment)
            }
            return view
        }

}




