package com.example.groceryshoppingapplication.adapters

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
    private val categoryNames: List<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolderTitles(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.searchView_search_item
    }

    class ViewHolderCategories(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
                (holder as ViewHolderTitles).title.text = productNames.get(position)
            }
            in (productNames.size + 1)..(productNames.size + categoryNames.size) -> {
                (holder as ViewHolderTitles).title.text = categoryNames.get(position)
            }
        }
    }

    override fun getItemCount() = productNames.size + categoryNames.size

    override fun getItemViewType(position: Int): Int {
        if (position != productNames.size)
            return 0
        return 1
    }
}