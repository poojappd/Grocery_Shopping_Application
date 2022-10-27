package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R

class RecentSearchAdapter (private val searchTexts: List<String>,private val queryListener:(String)->Unit )
    : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.recentSearchItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_search_single_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchText = searchTexts.get(position)
        holder.title.text = searchText
        holder.title.setOnClickListener { queryListener(searchText) }

    }

    override fun getItemCount(): Int {
        return searchTexts.size

    }
}