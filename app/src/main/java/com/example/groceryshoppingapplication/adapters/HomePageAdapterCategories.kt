package com.example.groceryshoppingapplication.adapters

import android.app.Application
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapConverter
import com.example.groceryshoppingapplication.enums.GeneralCategory
import kotlinx.android.synthetic.main.homepage_image_categories_layout.view.*

class HomePageAdapterCategories() :
    RecyclerView.Adapter<HomePageAdapterCategories.ViewHolder>() {
    private val titles = enumValues<GeneralCategory>()
    //vegetables, beverages, meat
    private val bgColorValues = arrayOf("#C9E265","#D6F8E8","#FFE3CF",)
    private val fontColorValues = arrayOf("#48722E","#678586","#FFE3CF")

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.categ_image_homepage
        val title = view.categ_title_homepage
        val bd = view.bg_categ_img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.homepage_image_categories_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = BitmapConverter.categImgHome!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = BitmapConverter.getBitmapFromImageAsset(position)
        holder.image.setImageBitmap(bitmap)
        val title = if(position >= titles.size) "Fruits" else if (position ==1)"Dairy and bakery" else titles.get(position).value
        holder.title.text = title
        val shapreDrawableBg = holder.bd.background as (GradientDrawable)
        shapreDrawableBg.setColor(Color.parseColor("#e8ffc7"))

    }


}