package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory
import com.example.groceryshoppingapplication.fragments.WishListFragment
import com.example.groceryshoppingapplication.models.WishListItemEntity
import kotlinx.android.synthetic.main.wishlist_single_row.view.*
import java.text.DecimalFormat

class WishListItemsAdapter(val wishListItems: List<WishListItemEntity>, val wishListListener: WishListFragment.WishListTouchListenerImpl):RecyclerView.Adapter<WishListItemsAdapter.WishListViewHolder>() {

    class WishListViewHolder(view:View): RecyclerView.ViewHolder(view){
        val imageView = view.product_image
        val titleTv = view.product_title
        val capacityTv = view.qty_wishlist
        val priceTv = view.ItemPrice_wishList
        val wishListButton = view.wishList_button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_single_row, parent, false)
        return WishListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val wishListItem = wishListItems[position]
        val image = BitmapFactory.getProductBitmapFromAsset(wishListItem.productCode.toString(),0)
        val extras  = wishListListener.getWishListItemExtras(wishListItem.productCode)
        val decimal = DecimalFormat("#.00")

        holder.apply {
            imageView.setImageBitmap(image)
            titleTv.text = extras.productTitle
            capacityTv.text =  StringBuilder().append("${extras.productPrice} ${extras.quantityUnit}")
            priceTv.text = decimal.format(extras.productPrice)
            wishListButton.setOnClickListener {
                val wishListRemoveAnim =   AnimationUtils.loadAnimation(
                    wishListListener.getContext(),
                    R.anim.add_to_cart_icon_in_product_animation
                )
                wishListRemoveAnim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        wishListListener.removeFromWishList(wishListItem.id)

                    }
                })
                it.startAnimation(
                  wishListRemoveAnim
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return wishListItems.size
    }
}