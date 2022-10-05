package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.groceryshoppingapplication.R
import kotlinx.android.synthetic.main.fragment_edit_address.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*

class EditAddressFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_address, container, false)
        val cities = requireContext().resources.getStringArray(R.array.cities)
        val adapter = ArrayAdapter(this.requireContext(),R.layout.cities_array_adapter, cities)
        view.city_autocomplete.setAdapter(adapter)
        return view
    }


}