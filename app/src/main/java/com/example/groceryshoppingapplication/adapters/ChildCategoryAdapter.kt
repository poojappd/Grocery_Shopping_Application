package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.SubCategory

class ChildCategoryAdapter(private val categoryDataHolder: List<SubCategory>, private val navigationListener: (SubCategory)->Unit) : RecyclerView.Adapter<ChildCategoryAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.sub_categ_title)
        val layout = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_category_single_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = categoryDataHolder.get(position).value
        holder.itemView.setOnClickListener {
            navigationListener(categoryDataHolder.get(position))

        }
    }

    override fun getItemCount(): Int {
        return categoryDataHolder.size

    }
}