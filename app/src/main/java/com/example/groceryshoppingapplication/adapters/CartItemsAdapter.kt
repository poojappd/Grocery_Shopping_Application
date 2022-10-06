package com.example.groceryshoppingapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.CartItemData
import com.example.groceryshoppingapplication.CartItemTouchListener
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapConverter.getBitmapFromAsset
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.cart_single_item.view.*

class CartItemsAdapter(
    private val cartItems: List<CartItemEntity>,
    private val cartItemTouchListener: CartItemTouchListener
) :
    RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_single_item, parent, false)
        return CartItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val productCode = cartItems[position].productCode
        val extras = cartItemTouchListener.getCartItemExtraData(productCode)
        holder.apply {
            title.text = extras.productTitle
            price.text = extras.productPrice.toString()
            count.text = cartItems.get(position).quantity.toString()
            image.setImageBitmap(
                getBitmapFromAsset(
                    cartItems.get(position).productCode.toString(),
                    true
                )
            )
            increaseButton.setOnClickListener {
                //userViewModel.addToCart(productCode)
                cartItemTouchListener.addToCart(productCode)

            }

            decreaseButton.setOnClickListener {
                cartItemTouchListener.removeFromCart(productCode)
            }

        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CartItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.cartItem_title
        val count = view.cartItem_quantity
        val price = view.cartItem_price
        val image = view.cartItem_image
        val increaseButton = view.shapeableImageView
        val decreaseButton = view.shapeableImageView2
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}