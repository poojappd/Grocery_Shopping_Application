package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.Utils.BitmapConverter.getBitmapFromAsset
import kotlinx.android.synthetic.main.view_pager_item_image.view.*
import java.io.IOException
import java.io.InputStream

class ProductViewPagerAdapter(private val productCode:Int,private val imagesList: Array<String>) :
    RecyclerView.Adapter<ProductViewPagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.product_image_in_viewpager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_pager_item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = getBitmapFromAsset(productCode.toString(), false, imagesList.get(position))
        holder.image.setImageBitmap(bitmap)
    }

    override fun getItemCount() = imagesList.size


}