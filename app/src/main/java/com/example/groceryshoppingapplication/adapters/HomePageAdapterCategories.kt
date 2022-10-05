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
   // private val titles = enumValues<GeneralCategory>()
    private val categoryNames = "Fruits Vegetables Dairy Meat_&_Eggs Beverages Snacks_&_Packed_foods Beauty_&_Hygiene Cleaning_&_Household Pets Gardening".split(" ")
    private val bgColorValues = arrayOf("#FEE2FF","#C9E265","#EBE6E0","#FFE3CF","#D6F8E8","#FFFAC3","#237F82","#F2E3F8","#CBBFC3","#EBDBB7",)
    private val fontColorValues = arrayOf("#583670","#48722E","#5E8E8C","#A66054","#678586","#237F82","#CBFDFF","#613A69","#A66054","#8B5F4C",)

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
        val title =  categoryNames.get(position).replace("_", " ")
        holder.title.text = title
        val shapreDrawableBg = holder.bd.background as (GradientDrawable)
        shapreDrawableBg.setColor(Color.parseColor(bgColorValues[position]))
        holder.title.setTextColor(Color.parseColor(fontColorValues[position]))

    }


}