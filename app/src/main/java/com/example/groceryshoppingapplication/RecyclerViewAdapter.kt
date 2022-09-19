package com.example.groceryshoppingapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.groceryshoppingapplication.Utils.CategoryDataHolder

class RecyclerViewAdapter (val categoryDataHolders: MutableList<CategoryDataHolder>, val context: Context) :
    BaseAdapter() {
    private val layoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int = categoryDataHolders.size


    override fun getItem(p0: Int): Any {
        return categoryDataHolders[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, view: View?, container: ViewGroup?): View {
        val categoryViewHolder: CategoryViewHolder
        val view1: View
        if (view == null) {
            view1 = layoutInflater.inflate(R.layout.category_single_row,container,false )
            categoryViewHolder = CategoryViewHolder(view1)
            view1.tag = categoryViewHolder
        }
        else{
            view1 = view
            categoryViewHolder = view1.tag as CategoryViewHolder
        }

        val rowData = categoryDataHolders.get(p0)
        categoryViewHolder.iconView.setImageResource(rowData.iconId)
        categoryViewHolder.descriptionView.text = rowData.description
        return view1
    }

    private class CategoryViewHolder(val view: View) {
        val iconView = view.findViewById<ImageView>(R.id.category_icon)
        val descriptionView = view.findViewById<TextView>(R.id.category_description)

    }
}