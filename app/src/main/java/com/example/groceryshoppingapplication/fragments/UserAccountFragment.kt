package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.SharedPrefViewModel
import com.example.groceryshoppingapplication.SharedPrefViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_account.view.*

class UserAccountFragment : Fragment() {



    val viewModel: SharedPrefViewModel by activityViewModels {
        SharedPrefViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)
        view.textView17.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_addressesFragment)
        }
        view.EditButton.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_userDetailsEditFragment)
        }
        view.textView19.setOnClickListener {

            val sharedPref =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            sharedPref.edit().apply {
                clear()
                apply()
            }
            viewModel.userMobile = null
            findNavController().navigate(R.id.action_userAccountFragment_to_loginScreenFragment)



        }
        view.textView16.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_ordersViewPagerFragment)
        }
        view.textView18.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_wishListFragment)
        }
        return view
    }
}