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
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_place_order.view.*
import kotlinx.android.synthetic.main.fragment_user_account.view.*

class UserAccountFragment : Fragment() {
    val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)

        val currentUser = userViewmodel.currentUser.value!!
        val userAddresses = userViewmodel.currentUserAddresses.value!!
        val currentUserAddress  = if (userAddresses.isEmpty()) null else userAddresses.get(0)

        val userFullName = ((currentUser.firstName ) ?: "-") + (currentUser.lastName?:"")
        view.textView20.text = userFullName
        view.textView21.text = currentUser.mobileNumber
        currentUserAddress?.let {
            view.textView9.text = it.areaDetails
            view.cityPincode.text = StringBuilder().append(it.city+" - "+it.pincode)
        } ?: run{
            view.textView9.text = "-"
            view.cityPincode.text =""
        }
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