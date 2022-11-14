package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.listeners.CategoryItemTouchListener
import kotlinx.android.synthetic.main.category_single_row.view.*


class ParentCategoryAdapter(
    val parentCategoryData: Map<GeneralCategory,List<SubCategory>>,
    val categoryImages: Array<Int>,
    val expandAll:Boolean,
    val listener: CategoryItemTouchListener
) : RecyclerView.Adapter<ParentCategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(com.example.groceryshoppingapplication.R.layout.category_single_row, parent, false)
        if (expandAll) {
            view.nested_rv.visibility = View.VISIBLE
        }
        else{
            view.nested_rv.visibility = View.GONE
        }
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(categoryViewHolder: CategoryViewHolder, position: Int) {
        val rowData = parentCategoryData.keys.toList().get(position)
        categoryViewHolder.iconView.setImageResource(categoryImages[position])
        categoryViewHolder.descriptionView.text = rowData.value


        //onclick listener for category dropdown
        categoryViewHolder.pressedLayout.setOnClickListener {
            val slideUp = AnimationUtils.loadAnimation(categoryViewHolder.itemView.context, com.example.groceryshoppingapplication.R.anim.slide_in_vertical)
            when (categoryViewHolder.nestedRV.visibility) {
                View.GONE -> {
                    categoryViewHolder.nestedRV.visibility = View.VISIBLE
                    categoryViewHolder.dropDownIcon.setImageResource(com.example.groceryshoppingapplication.R.drawable.dropup_icon)
                }
                else -> {
                    //TransitionManager.endTransitions(categoryViewHolder.pressedLayout)
                    categoryViewHolder.nestedRV.visibility = View.GONE
                    categoryViewHolder.dropDownIcon.setImageResource(R.drawable.dropdown_icon)
notifyItemChanged(position)
                }
            }

        }

        val adapterMember = ChildCategoryAdapter(parentCategoryData.get(rowData)!!){subCategory:SubCategory->listener.navigateToFragment(subCategory)}
        val linearLayoutManager = LinearLayoutManager(listener.getContext())

        categoryViewHolder.nestedRV.layoutManager = linearLayoutManager
        categoryViewHolder.nestedRV.adapter = adapterMember
        categoryViewHolder.nestedRV.isNestedScrollingEnabled = false
    }

    override fun getItemCount(): Int {
        return parentCategoryData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconView = itemView.findViewById<ImageView>(com.example.groceryshoppingapplication.R.id.category_icon)
        val descriptionView = itemView.findViewById<TextView>(com.example.groceryshoppingapplication.R.id.category_description)
        val nestedRV = itemView.findViewById<RecyclerView>(com.example.groceryshoppingapplication.R.id.nested_rv)
        val pressedLayout = itemView.findViewById<LinearLayout>(R.id.category_single_row_item)
        val dropDownIcon = itemView.findViewById<ImageView>(com.example.groceryshoppingapplication.R.id.drop_down_icon_iv)
    }


}