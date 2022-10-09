package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues.TAG
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
    private val cartItemTouchListener: CartItemTouchListener,
    private val cartItemTotalProceListener: (Double)-> Unit
) :
    RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>() {
    private var increasedPrice = BooleanArray(cartItems.size){false}

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
            if(!increasedPrice[position]) {
                price.text = extras.productPrice.toString()
            }
            count.text = cartItems.get(position).quantity.toString()
            image.setImageBitmap(
                getBitmapFromAsset(
                    cartItems.get(position).productCode.toString(),
                    true
                )
            )
            increaseButton.setOnClickListener {
                //userViewModel.addToCart(productCode)
                increasedPrice[position] = true
                cartItemTouchListener.addToCart(productCode)
                val newPrice = ((price.text as String).toDouble() * count.text.toString().toInt())
                price.text = newPrice.toString()
                Log.e(TAG, "increased to ---"+newPrice.toString())
            }

            decreaseButton.setOnClickListener {
                cartItemTouchListener.removeFromCart(productCode)
                val newPrice = ((price.text as String).toDouble() / count.text.toString().toInt())
                Log.e(TAG, "decreased to ---"+newPrice.toString())

                price.text = newPrice.toString()
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