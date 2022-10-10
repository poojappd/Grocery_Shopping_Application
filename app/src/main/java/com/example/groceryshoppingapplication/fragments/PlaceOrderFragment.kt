package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.groceryshoppingapplication.R

class PlaceOrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_order, container, false)
    }

}
// <rotate
//
//                android:fromDegrees="45"
//                android:toDegrees="45"
//                android:pivotX="60%"
//                android:pivotY="10%" >