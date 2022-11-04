package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.listeners.ProductListTouchListener
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.BitmapFactory.getProductBitmapFromAsset
import com.example.groceryshoppingapplication.enums.ProductAvailability
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import kotlinx.android.synthetic.main.single_product_row_item_in_list.view.*
import java.lang.StringBuilder
import java.text.DecimalFormat

class ProductsInCategoriesAdapter(
    private val products: List<GroceryItemEntity>,
    private val context: Context,
    private val productListTouchListener: ProductListTouchListener

) :
    RecyclerView.Adapter<ProductsInCategoriesAdapter.ProductViewHolder>() {
    private val buttonClickedStates = mutableMapOf<Int, Boolean>()

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandName = view.product_brand_textView
        val productName = view.product_name_textView
        val price = view.product_price_textView
        val image = view.product_image_in_list
        val addToCartButton: ImageView = view.add_to_cart_button_productList
        val container = view.product_list_item_container
        val notAvailableBanner = view.notAvailableBAnner_productList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        Log.e(TAG,"INSIDE PRODUCT LIST ADAPTER")

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.single_product_row_item_in_list, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val decimal = DecimalFormat("0.##")

        holder.apply {
            brandName.text = products.get(position).brandName
            productName.text = products.get(position).itemName

            price.text = StringBuilder().append("Rs. "+decimal.format(products.get(position).unitPrice))
            Log.e(
                TAG,
                "position - ${products.get(position).brandName}  ${
                    buttonClickedStates.get(
                        products.get(position).productCode
                    )
                }"
            )

            image.setImageBitmap(
                getProductBitmapFromAsset(products.get(position).productCode.toString(),0)
            )
            container.setOnClickListener {
               productListTouchListener.navigate(products.get(position).productCode)
            }

            val productCode = products.get(position).productCode
            var response = productListTouchListener.checkItemInCart(productCode)
            var buttonState = response != Response.NO_SUCH_ITEM_IN_CART
            toggleAddToCartButton(buttonState, addToCartButton)
            if(products[position].productAvailability==ProductAvailability.OUT_OF_STOCK) {
                notAvailableBanner.visibility = View.VISIBLE
                addToCartButton.isEnabled = false
                addToCartButton.setImageResource(R.drawable.cancel_add_to_cart_icon);
            }
            else {
                addToCartButton.setOnClickListener {
                    response = productListTouchListener.checkItemInCart(productCode)
                    buttonState = response != Response.NO_SUCH_ITEM_IN_CART
                    toggleAddToCartButton(!buttonState, addToCartButton, productCode)

                    it.startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.add_to_cart_icon_in_product_animation
                        )
                    )

                }
            }
        }

    }


    override fun getItemCount(): Int {
        return products.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun toggleAddToCartButton(state:Boolean, addToCartButton:ImageView, productCode:Int=-1){
        if (state) {
            addToCartButton.setColorFilter(Color.argb(255, 255, 255, 255));
            addToCartButton.setBackgroundResource(R.drawable.product_list_add_to_cart_icon_clicked_bg)
            if(productCode!=-1) productListTouchListener.addToCart(productCode)
        } else {
            addToCartButton.setColorFilter(Color.argb(255, 164, 191, 246));
            addToCartButton.setBackgroundResource(R.drawable.product_list_add_to_cart_icon_bg)
            if(productCode!=-1) productListTouchListener.removeFromCartCompletely(productCode)

        }
    }

}