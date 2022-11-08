package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.activityViewModels
import com.example.groceryshoppingapplication.ProductSearchResultsFragment
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.RefineViewPagerAdapter
import com.example.groceryshoppingapplication.viewmodels.FilterViewModel
import com.example.groceryshoppingapplication.viewmodels.FilterViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_product_refine.view.*


class ProductRefineFragment() : BottomSheetDialogFragment() {
    private val filterViewModel: FilterViewModel by activityViewModels {
        FilterViewModelFactory()
    }

    companion object {
        const val FILTER_CALLBACK_FUNCTION: String = "FILTER_CALLBACK_FUNCTION"

        fun newInstance(filterListener: ProductSearchResultsFragment.SetFilter): ProductRefineFragment {
            val fragment = ProductRefineFragment()
            val bundle = Bundle()
            bundle.putSerializable(FILTER_CALLBACK_FUNCTION, filterListener)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "IN ONCREATE OF PRODUCT REFINE(BOTTOM SHEET DIALOG")
        if (dialog != null && dialog!!.getWindow() != null) {
            dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()!!.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_refine, container, false)
        val filterListener = arguments?.getSerializable(FILTER_CALLBACK_FUNCTION) as ProductSearchResultsFragment.SetFilter
        val viewPager = view.sortFilter_viewpager
        viewPager.adapter = RefineViewPagerAdapter(this)
        TabLayoutMediator(view.refine_tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Sort"
                else -> tab.text = "Filter"
            }
        }.attach()
        filterViewModel.appliedFilterConfiguration?.let {
            filterViewModel.temporaryFilterConfiguration = it

        }

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