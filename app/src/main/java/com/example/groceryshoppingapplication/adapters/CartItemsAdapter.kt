package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.listeners.CartItemTouchListener
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory.getProductBitmapFromAsset
import com.example.groceryshoppingapplication.models.CartItemEntity
import kotlinx.android.synthetic.main.cart_single_item.view.*
import java.text.DecimalFormat

class CartItemsAdapter(
    private val cartItems: List<CartItemEntity>,
    private val cartItemTouchListener: CartItemTouchListener,
    private val cartItemTotalPriceListener: (Double) -> Unit
) :
    RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>() {
    private lateinit var maxQuantityReachedToast:Toast

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_single_item, parent, false)
        maxQuantityReachedToast = Toast.makeText(parent.context,"Maximum ordering quantity for this product is 10",Toast.LENGTH_SHORT)
        return CartItemsViewHolder(view)

    }

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val productCode = cartItems[position].productCode
        val extras = cartItemTouchListener.getCartItemExtraData(productCode)
        val decimal = DecimalFormat("#.00")
        holder.apply {
            title.text = extras.productTitle
            val qty = cartItems.get(position).quantity
            price.text = decimal.format(extras.productPrice * qty)

            count.text = qty.toString()
            image.setImageBitmap(
                getProductBitmapFromAsset(
                    cartItems.get(position).productCode.toString(),
                    0
                )
            )
            increaseButton.setOnClickListener {
                //userViewModel.addToCart(productCode)
                if ((count.text as String).toInt() <10) {
                    cartItemTouchListener.addToCart(productCode)
                }
                else{
                    maxQuantityReachedToast.show()
                }
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