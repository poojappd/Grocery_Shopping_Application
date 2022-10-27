package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.GeneralCategory
import com.example.groceryshoppingapplication.enums.SubCategory
import com.example.groceryshoppingapplication.listeners.CategoryItemTouchListener

class ParentCategoryAdapter (
    val parentCategoryData: Map<GeneralCategory,List<SubCategory>>,
    val categoryImages: Array<Int>,
    val listener: CategoryItemTouchListener
) : RecyclerView.Adapter<ParentCategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.category_single_row, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(categoryViewHolder: CategoryViewHolder, position: Int) {
        val rowData = parentCategoryData.keys.toList().get(position)
        categoryViewHolder.iconView.setImageResource(categoryImages[position])
        categoryViewHolder.descriptionView.text = rowData.value


        //onclick listener for category dropdown
        categoryViewHolder.pressedLayout.setOnClickListener {

            TransitionManager.beginDelayedTransition(categoryViewHolder.pressedLayout)
            when (categoryViewHolder.nestedRV.visibility) {
                View.GONE -> {
                    categoryViewHolder.nestedRV.visibility = View.VISIBLE
                    categoryViewHolder.dropDownIcon.setImageResource(R.drawable.dropup_icon)
                }
                else -> {
                    categoryViewHolder.nestedRV.visibility = View.GONE
                    categoryViewHolder.dropDownIcon.setImageResource(R.drawable.dropdown_icon)
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
        val iconView = itemView.findViewById<ImageView>(R.id.category_icon)
        val descriptionView = itemView.findViewById<TextView>(R.id.category_description)
        val nestedRV = itemView.findViewById<RecyclerView>(R.id.nested_rv)
        val pressedLayout = itemView.findViewById<LinearLayout>(R.id.category_single_row_item)
        val dropDownIcon = itemView.findViewById<ImageView>(R.id.drop_down_icon_iv)
    }


}