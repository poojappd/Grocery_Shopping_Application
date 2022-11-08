package com.example.groceryshoppingapplication.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.icu.lang.UCharacter.JoiningGroup.PE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.fragments.HomePageFragment
import kotlinx.android.synthetic.main.homepage_image_categories_layout.view.*


class HomePageAdapterCategories(private val categoryTouchListener:HomePageFragment.CategoryItemTouchListenerImpl) :
    RecyclerView.Adapter<HomePageAdapterCategories.ViewHolder>() {
   // private val titles = enumValues<GeneralCategory>()
    private val categoryNames = "Fruits Vegetables Dairy Meat_&_Eggs Beverages Staples Snacks_&_Packed_foods Beauty_&_Hygiene Cleaning_&_Household Pets Gardening".split(" ")
    private val categoryEnums = arrayOf(GeneralCategory.FRUITS,GeneralCategory.VEGETABLES, GeneralCategory.DAIRY_AND_BAKERY, GeneralCategory.MEAT_AND_EGGS, GeneralCategory.BEVERAGES, GeneralCategory.STAPLES, GeneralCategory.SNACKS_AND_BRANDED_FOODS, GeneralCategory.BEAUTY_AND_HYGIENE, GeneralCategory.CLEANING_AND_HOUSEHOLD, GeneralCategory.PETS, GeneralCategory.GARDENING)
    private val bgColorValues = arrayOf(
        "#FEE2FF",
        "#E5FFA4",
        "#EBE6E0",
        "#FFF1EA",
        "#D6F8E8",
        "#F0FAEB",
        "#FFFAC3",
        "#237F82",
        "#F2E3F8",
        "#CBBFC3",
        "#EBDBB7"
    )
    private val fontColorValues = arrayOf(
        "#583670",
        "#48722E",
        "#5E8E8C",
        "#A66054",
        "#678586",
        "#3A4037",
        "#237F82",
        "#CBFDFF",
        "#613A69",
        "#A66054",
        "#8B5F4C"
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.categ_image_homepage
        val title = view.categ_title_homepage
        val bg = view.bg_categ_img
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.homepage_image_categories_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = BitmapFactory.categImgHome!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = BitmapFactory.getCategoryBitmapFromAsset(position)
        holder.image.setImageBitmap(bitmap)
        val title =  categoryNames.get(position).replace("_", " ")
        if(title.length > 12)
            holder.title.textSize = 18f
        holder.title.text = title
        val shapreDrawableBg = holder.bg.background as (GradientDrawable)
        shapreDrawableBg.setColor(Color.parseColor(bgColorValues[position]))
        holder.title.setTextColor(Color.parseColor(fontColorValues[position]))
        val fadingEffect = AlphaAnimation(1f, 0.8f)

        holder.bg.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(
                categoryTouchListener.getContext(),
                com.google.android.material.R.anim.abc_grow_fade_in_from_bottom
            )
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    categoryTouchListener.navigateToProductsByGeneralCategory(categoryEnums[position])

                }
            })
            it.startAnimation(
                animation
            )
        }
    }


}