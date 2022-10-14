package com.example.groceryshoppingapplication.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.groceryshoppingapplication.fragments.OrdersViewPagerFragment

class OrdersViewPagerFragmentAdapter(private val fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1-> OrdersViewPagerFragment(fragment.requireContext().applicationContext)
            else -> OrdersViewPagerFragment(fragment.requireContext().applicationContext)
        }
    }
}