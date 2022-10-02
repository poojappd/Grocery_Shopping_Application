package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.ChildCategoryRowData
import com.example.groceryshoppingapplication.R

class ChildCategoryAdapter(private val navController: NavController, val categoryDataHolder: List<ChildCategoryRowData>) : RecyclerView.Adapter<ChildCategoryAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.sub_categ_title)
        val layout = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_single_row, parent, false)

        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = categoryDataHolder.get(position).title
        holder.itemView.setOnClickListener {
            Log.e(ContentValues.TAG,"clicked subtitle - ${categoryDataHolder.get(position).title}")
            navController.navigate(R.id.action_allCategoriesFragment_to_productsListFragment)

        }
        Log.e(ContentValues.TAG,"${categoryDataHolder.get(position)} \n$position")
    }

    override fun getItemCount(): Int {
        return categoryDataHolder.size

    }
}