package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.viewmodels.FilterViewModel
import com.example.groceryshoppingapplication.viewmodels.FilterViewModelFactory
import kotlinx.android.synthetic.main.fragment_sort_view_pager.view.*


class SortViewPagerFragment : Fragment() {
    private val viewmodel: FilterViewModel by activityViewModels {
        FilterViewModelFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sort_view_pager, container, false)
        viewmodel.appliedFilterConfiguration?.let {
            it.temporarySortPriceLowToHigh?.let {
                if(it== true) view.lthRadio.isChecked = true else view.htlRadio.isChecked = true
            }
        }
        view.lthRadio.setOnClickListener{
            viewmodel.temporaryFilterConfiguration?.temporarySortPriceLowToHigh = true
        }
        view.htlRadio.setOnClickListener {
            viewmodel.temporaryFilterConfiguration?.temporarySortPriceLowToHigh = false
        }
        return view
    }

}