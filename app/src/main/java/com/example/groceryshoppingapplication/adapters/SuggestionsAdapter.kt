package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory
import kotlinx.android.synthetic.main.search_suggestions_single_row.view.*
import kotlinx.android.synthetic.main.view_pager_item_image.view.*

class SuggestionsAdapter(
    private val productNames: List<String>,
    private val categoryNames: List<String>,
    private val searchTouchListener: (String,Int,Boolean)->Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        var c = 1
    }

    class ViewHolderTitles(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.searchView_search_item
    }

    class ViewHolderCategories(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e(TAG,"INSIDE SUGGESTIONS ADAPTER")
        if (viewType != 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_suggestions_single_row, parent, false)
            return ViewHolderTitles(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_suggestions_category, parent, false)

            return ViewHolderCategories(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            in productNames.indices -> {
                val searchResult = productNames.get(position)
                (holder as ViewHolderTitles).title.text = searchResult
                holder.itemView.setOnClickListener {
                    searchTouchListener(searchResult,position,true)
                }
            }
            in (productNames.size + 1)..(productNames.size + categoryNames.size) -> {
                val categIndex = position % categoryNames.size
                val searchResult = categoryNames.get(categIndex)
                (holder as ViewHolderTitles).title.text = searchResult
                holder.itemView.setOnClickListener {
                    searchTouchListener(searchResult, categIndex, false)
                }
            }
        }

    }

    override fun getItemCount() = productNames.size + categoryNames.size +1

    override fun getItemViewType(position: Int): Int {
        if (position != productNames.size)
            return 0
        return 1
    }
}