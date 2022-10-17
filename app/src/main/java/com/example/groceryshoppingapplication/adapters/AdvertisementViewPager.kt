package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory
import kotlinx.android.synthetic.main.view_pager_item_image.view.*


class AdvertisementViewPager:
    RecyclerView.Adapter<AdvertisementViewPager.ViewHolder>() {
    private val imagesList = BitmapFactory.getAdvertisementBitmapFromAsset()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.product_image_in_viewpager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_pager_item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = imagesList[position]
        holder.image.setImageBitmap(bitmap)
    }

    override fun getItemCount() = imagesList.size


}