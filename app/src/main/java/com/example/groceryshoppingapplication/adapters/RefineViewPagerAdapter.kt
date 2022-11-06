package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.groceryshoppingapplication.fragments.FilterViewPagerFragment
import com.example.groceryshoppingapplication.fragments.OrdersViewPagerFragment
import com.example.groceryshoppingapplication.fragments.PlaceOrderFragment
import com.example.groceryshoppingapplication.fragments.SortViewPagerFragment

class RefineViewPagerAdapter(private val fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        Log.e(TAG,"INSIDE REFIN VIEWPAGER ADAPTER $position")
        return when(position){
            0-> SortViewPagerFragment()
            else -> FilterViewPagerFragment()
        }
    }

}
