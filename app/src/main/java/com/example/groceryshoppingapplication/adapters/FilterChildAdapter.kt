package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.filter_child_recylerview_single_row.view.*

class FilterChildAdapter(private val filterTitle:List<String>,
                         private val selectedFilterTitles:List<Int>? = null,
                         private val filterTouchListener:(Int, Boolean)->Unit
): RecyclerView.Adapter<FilterChildAdapter.FilterChildViewHolder>() {

    class FilterChildViewHolder(view:View):RecyclerView.ViewHolder(view){
        val checkBox = view.childFilter_checkBox
        val title: MaterialTextView = view.childFilter_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_child_recylerview_single_row,parent,false)
        return FilterChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterChildViewHolder, position: Int) {
        holder.apply {
            title.text = filterTitle.get(position)
            if(selectedFilterTitles?.contains(position) == true){
                checkBox.isChecked = true
            }
            checkBox.setOnClickListener {
                filterTouchListener(position, checkBox.isChecked)
            }
        }
    }

    override fun getItemCount() = filterTitle.size
}