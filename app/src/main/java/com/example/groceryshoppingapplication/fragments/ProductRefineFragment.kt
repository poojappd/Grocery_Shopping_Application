package com.example.groceryshoppingapplication.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.groceryshoppingapplication.ProductSearchResultsFragment
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.OrdersViewPagerFragmentAdapter
import com.example.groceryshoppingapplication.adapters.RefineViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_orders.view.*
import kotlinx.android.synthetic.main.fragment_product_refine.view.*


class ProductRefineFragment(private val filterListener: ProductSearchResultsFragment.SetFilter) : DialogFragment() {
    override fun onStart() {
        super.onStart()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        dialog?.window?.setLayout(width - 58,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog!!.getWindow() != null) {
            dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()!!.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_refine, container, false)
        val viewPager = view.sortFilter_viewpager
        viewPager.adapter = RefineViewPagerAdapter(this)
        TabLayoutMediator(view.refine_tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Sort"
                else -> tab.text = "Filter"
            }
        }.attach()
        view.applyFilter.setOnClickListener {
            filterListener.applyFilter()
            this.dismiss()
        }
        view.clearFilter.setOnClickListener {
            filterListener.clearFilter()
            this.dismiss()
        }
    return view
    }


}