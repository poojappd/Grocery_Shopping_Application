package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
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
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.VISIBLE
        val view =  inflater.inflate(R.layout.fragment_home_page, container, false)
        val someButton = view.findViewById<Button>(R.id.goTo)
        val text = view.userNamme
        val res = viewmodel.loginUser("6000000000")
         viewmodel.currentUser.observe(viewLifecycleOwner) {
            text.setText(it.firstName.toString())
             Log.e(TAG, viewmodel.currentUserCart.value.toString())
        }
        //userViewmodel.changeName("ch")



        someButton.setOnClickListener {
                findNavController().navigate(R.id.action_homePageFragment_to_allCategoriesFragment)
            }
            return view
        }

}




