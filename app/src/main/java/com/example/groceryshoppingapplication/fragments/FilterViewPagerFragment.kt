package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.FilterChildAdapter
import com.example.groceryshoppingapplication.viewmodels.FilterViewModel
import com.example.groceryshoppingapplication.viewmodels.FilterViewModelFactory
import kotlinx.android.synthetic.main.fragment_filter_view_pager.*
import kotlinx.android.synthetic.main.fragment_filter_view_pager.view.*

class FilterViewPagerFragment : Fragment() {
    private val viewmodel: FilterViewModel by activityViewModels {
        FilterViewModelFactory()
    }

    private lateinit var categRV: RecyclerView
    private lateinit var brandRV: RecyclerView
    private lateinit var sizeRV: RecyclerView
    private var lastDropDownImage: ImageView? = null
    private var lastExpandedRecyclerView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_filter_view_pager, container, false)
        //viewmodel.clearTemporaryConfiguration()

        var packSizePositions: List<Int>? = null
        var categPositions: List<Int>? = null
        var brandPositions: List<Int>? = null

        viewmodel.appliedFilterConfiguration?.let {
            packSizePositions = it.packSizePositions
            categPositions = it.categPositions
            brandPositions = it.brandPositions
        }

        view.apply {
            setBadge(brand_badge, viewmodel.brandFilterCount)
            setBadge(categ_badge, viewmodel.categFilterCount)
            setBadge(size_badge, viewmodel.sizeFilterCount)
        }




        categRV = view.categ_child_recyclerView_ViewPager
        categRV.layoutManager = LinearLayoutManager(requireContext())
        categRV.adapter = FilterChildAdapter(
            viewmodel.categoryString,
            selectedFilterTitles = categPositions
        ) { position, isSelected ->
            viewmodel.temporaryFilterConfiguration?.setCategory(position, isSelected)
        }

        brandRV = view.brand_child_recyclerView_ViewPager
        brandRV.layoutManager = LinearLayoutManager(requireContext())
        brandRV.adapter =
            FilterChildAdapter(viewmodel.brands, brandPositions) { position, isSelected ->
                viewmodel.temporaryFilterConfiguration?.setBrand(position, isSelected)
                Log.e(TAG, "CHANGING BRAND")
            }

        sizeRV = view.size_child_recyclerView_ViewPager
        sizeRV.layoutManager = LinearLayoutManager(requireContext())
        sizeRV.adapter =
            FilterChildAdapter(viewmodel.packSizes, packSizePositions) { position, isSelected ->
                viewmodel.temporaryFilterConfiguration?.setPackSize(position, isSelected)
            }
        view.constraintLayout8.setOnClickListener {
            expandDropDownList(categRV, view.categ_drop_down)
        }
        view.constraintLayout9.setOnClickListener {
            Log.e(TAG, "${lastExpandedRecyclerView?.id}-last; ${brandRV.id}  ")
            expandDropDownList(brandRV, view.brand_drop_down)
        }
        view.packSizeContainer.setOnClickListener {
            expandDropDownList(sizeRV, view.size_drop_down)
        }
        return view
    }

    fun expandDropDownList(targetRecyclerView: RecyclerView, targetDropDownImageView: ImageView) {
        lastExpandedRecyclerView?.let {
            if (it.id != targetRecyclerView.id) {
                it.visibility = View.GONE
                lastDropDownImage?.setImageResource(R.drawable.dropdown_icon)

                targetRecyclerView.visibility = View.VISIBLE
                targetDropDownImageView.setImageResource(R.drawable.dropup_icon)
            } else {
                if (it.visibility == View.VISIBLE) {
                    targetRecyclerView.visibility = View.GONE
                    targetDropDownImageView.setImageResource(R.drawable.dropdown_icon)

                } else {
                    targetRecyclerView.visibility = View.VISIBLE
                    targetDropDownImageView.setImageResource(R.drawable.dropup_icon)
                }

            }
        } ?: run {
            targetRecyclerView.visibility = View.VISIBLE
            targetDropDownImageView.setImageResource(R.drawable.dropup_icon)
        }
        lastDropDownImage = targetDropDownImageView
        lastExpandedRecyclerView = targetRecyclerView

    }

    fun setBadge(badgeView: TextView, badgeCount: Int) {
        if (badgeCount > 0) {
            badgeView.text = badgeCount.toString()
        } else
            badgeView.isVisible = false
    }

}


