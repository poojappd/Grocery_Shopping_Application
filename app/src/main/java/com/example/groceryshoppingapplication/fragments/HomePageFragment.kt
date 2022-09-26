package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.data.AppDatabase


class HomePageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home_page, container, false)
        val someButton = view.findViewById<Button>(R.id.goTo)
        val db = AppDatabase.getDatabase(requireContext())
        val dao = db.getInventoryDao()
        Log.e(
            TAG,
            "Item is " + dao.getAllUsers()
        )
        for (user in dao.getAllUsers()) {
            Log.e(
                "USERINFO",
                "Item is " + user.name
            )
        }


        someButton.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_allCategoriesFragment)
        }
        return view
    }




}