package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.FilterChildAdapter
import com.example.groceryshoppingapplication.viewmodels.FilterViewModel
import com.example.groceryshoppingapplication.viewmodels.FilterViewModelFactory
import kotlinx.android.synthetic.main.fragment_filter_view_pager.view.*

class FilterViewPagerFragment : Fragment() {
    private val viewmodel: FilterViewModel by activityViewModels {
        FilterViewModelFactory()
    }

    private lateinit var categRV: RecyclerView
    private lateinit var brandRV: RecyclerView
    private lateinit var sizeRV: RecyclerView
    private var lastExpandedRecyclerView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_filter_view_pager, container, false)
        viewmodel.clearTemporaryConfiguration()
        categRV = view.categ_child_recyclerView_ViewPager
        categRV.layoutManager = LinearLayoutManager(requireContext())
        var packSizePositions: List<Int>? = null
        var categPositions: List<Int>? = null
        var brandPositions: List<Int>? = null

        viewmodel.appliedFilterConfiguration?.let {
            packSizePositions = it.packSizePositions
            categPositions = it.categPositions
            brandPositions = it.brandPositions
            viewmodel.temporaryFilterConfiguration = it

        }
        categRV.adapter = FilterChildAdapter(viewmodel.categoryString, selectedFilterTitles = categPositions){position->
            viewmodel.temporaryFilterConfiguration?.setCategory(position)
        }
        brandRV = view.brand_child_recyclerView_ViewPager
        brandRV.layoutManager = LinearLayoutManager(requireContext())
        brandRV.adapter = FilterChildAdapter(viewmodel.brands, brandPositions){position->
            viewmodel.temporaryFilterConfiguration?.setBrand(position)
        }
        sizeRV = view.size_child_recyclerView_ViewPager
        sizeRV.layoutManager = LinearLayoutManager(requireContext())
        sizeRV.adapter = FilterChildAdapter(viewmodel.packSizes, packSizePositions){position->
            viewmodel.temporaryFilterConfiguration?.setPackSize(position)
        }
        view.categ_drop_down.setOnClickListener {
            lastExpandedRecyclerView?.let {
                if(it.id != categRV.id) {
                    it.visibility = View.GONE
                    categRV.visibility = View.VISIBLE
                }
                else{
                    if(it.visibility ==View.VISIBLE) categRV.visibility = View.GONE else categRV.visibility = View.VISIBLE
                }
            }?: run{categRV.visibility = View.VISIBLE }

                lastExpandedRecyclerView = categRV

        }
        view.brand_drop_down.setOnClickListener {
            Log.e(TAG,"${lastExpandedRecyclerView?.id}-last; ${brandRV.id}  ")
            lastExpandedRecyclerView?.let {
                if(it.id != brandRV.id) {
                    it.visibility = View.GONE
                    brandRV.visibility = View.VISIBLE
                }
                else{
                    if(it.visibility ==View.VISIBLE) brandRV.visibility = View.GONE else brandRV.visibility = View.VISIBLE
                }
            } ?: run{brandRV.visibility = View.VISIBLE }

                lastExpandedRecyclerView = brandRV

        }
        view.size_drop_down.setOnClickListener {
            lastExpandedRecyclerView?.let {
                if(it.id != sizeRV.id) {
                    it.visibility = View.GONE
                    sizeRV.visibility = View.VISIBLE
                }
                else{
                    if(it.visibility ==View.VISIBLE) sizeRV.visibility = View.GONE else sizeRV.visibility = View.VISIBLE

                }
            } ?: run{sizeRV.visibility = View.VISIBLE }

                lastExpandedRecyclerView = sizeRV

        }
        return view
    }

}