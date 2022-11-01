package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory
import kotlinx.android.synthetic.main.unavailable_products_single_row.view.*

class UnavailableItemsAdapter(private val productCodes: List<String>,private val titles:List<String>)
    : RecyclerView.Adapter<UnavailableItemsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.unavailable_product_title
        val image = view.unavailable_product_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.unavailable_products_single_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = titles[position]
        holder.image.setImageBitmap(BitmapFactory.getProductBitmapFromAsset(productCodes[position],0))

    }

    override fun getItemCount(): Int {
        return titles.size

    }
}