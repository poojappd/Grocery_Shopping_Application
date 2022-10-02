package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapConverter
import kotlinx.android.synthetic.main.homepage_image_categories_layout.view.*

class HomePageAdapterCategories () :
    RecyclerView.Adapter<HomePageAdapterCategories.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.categ_image_homepage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.homepage_image_categories_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = BitmapConverter.categImgHome!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = BitmapConverter.getBitmapFromImageAsset(position
        )
        holder.image.setImageBitmap(bitmap)     }


}