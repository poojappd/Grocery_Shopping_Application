package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import kotlinx.android.synthetic.main.fragment_user_account.view.*

class UserAccountFragment : Fragment() {



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
           // requireActivity().getSharedPreferences()
        }
        return view
    }
}