package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory
import com.example.groceryshoppingapplication.fragments.ModifyOrderFragment
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import kotlinx.android.synthetic.main.modify_order_recyclerview_layout.view.*
import java.text.DecimalFormat

class ModifyOrderedItemsAdapter(
    val orderedItems:List<OrderedItemEntity>,
    val modifyItemListener: ModifyOrderFragment.ModifyItemListenerImpl
) : RecyclerView.Adapter<ModifyOrderedItemsAdapter.ModifyOrderedItemsViewHolder>() {


    class ModifyOrderedItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.modifyItem_image
        val titleTv = view.modifyItem_title
        val increaseQtyButton = view.shapeableImageView
        val decreaseQtyButton = view.shapeableImageView2
        val priceTv = view.modifyItem_price
        val deleteButton = view.deleteItem_modifyOrder
        val quantityTv = view.modifyItem_qty
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModifyOrderedItemsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.modify_order_recyclerview_layout, parent, false)
        return ModifyOrderedItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModifyOrderedItemsViewHolder, position: Int) {
        val position = holder.adapterPosition
        val orderListItem = orderedItems[position]
        val image = BitmapFactory.getProductBitmapFromAsset(orderListItem.productCode.toString(), 0)
        val extras = modifyItemListener.getOrderedItemExtras(orderListItem.productCode)

        holder.apply {
            imageView.setImageBitmap(image)
            titleTv.text = StringBuilder().append(extras.productTitle)
            quantityTv.text = orderListItem.quantity.toString()
            priceTv.text = modifyItemListener.decimal.format(extras.productPrice * orderListItem.quantity)

            deleteButton.setOnClickListener {
                val removeAnim = AnimationUtils.loadAnimation(
                    modifyItemListener.getContext(),
                    R.anim.add_to_cart_icon_in_product_animation

                )
                removeAnim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        modifyItemListener.removeFromOrder(orderListItem)
                        notifyItemRemoved(position)
                    }
                })
                it.startAnimation(
                    removeAnim
                )
            }

            increaseQtyButton.setOnClickListener {
                modifyItemListener.increaseQuantity(position)
                notifyItemChanged(adapterPosition)
            }

            decreaseQtyButton.setOnClickListener {
                modifyItemListener.decreaseQuantity(position)
                notifyItemChanged(adapterPosition)
            }

        }

    }

    override fun getItemCount(): Int {
        Log.e(TAG,orderedItems.size.toString()+" ----- item count")
        return orderedItems.size

    }

    override fun getItemViewType(position: Int): Int {
        return (position)
    }

    override fun getItemId(position: Int): Long {
        return (position.toLong())
    }
}