package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.Utils.BitmapConverter.getBitmapFromAsset
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.fragments.ProductsListFragmentDirections
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.single_product_row_item_in_list.view.*
import java.io.IOException
import java.io.InputStream

class ProductsInCategoriesAdapter(
    private val products: List<GroceryItemEntity>,
    private val context: Context,
    private val navController: NavController,
    private val viewModel:UserViewModel
) :
    RecyclerView.Adapter<ProductsInCategoriesAdapter.ProductViewHolder>() {
    private val mItemSelected = -1

    private val buttonClickedStates = mutableMapOf<Int, Boolean>()

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandName = view.product_brand_textView
        val productName = view.product_name_textView
        val price = view.product_price_textView
        val image = view.product_image_in_list
        val addToCartButton: ImageView = view.add_to_cart_button_productList
        val container = view.product_list_item_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.single_product_row_item_in_list, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.apply {
            brandName.text = products.get(position).brandName
            productName.text = products.get(position).itemName
            price.text = products.get(position).unitPrice.toString()
            Log.e(
                TAG,
                "position - ${products.get(position).brandName}  ${
                    buttonClickedStates.get(
                        products.get(position).productCode
                    )
                }"
            )

            image.setImageBitmap(
                getBitmapFromAsset(products.get(position).productCode.toString(),true)
            )
            container.setOnClickListener {
                val productCode = products.get(position).productCode
                val action =
                    ProductsListFragmentDirections.actionProductsListFragmentToSingleProductViewFragment(
                        productCode
                    )
                navController.navigate(action)
            }

            val productCode = products.get(position).productCode
            var response = viewModel.checkItemInCart(productCode)
            var buttonState = response != Response.NO_SUCH_ITEM_IN_CART
            toggleAddToCartButton(buttonState, addToCartButton)

            addToCartButton.setOnClickListener {
                it.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.add_to_cart_icon_in_product_animation
                    )
                )
                Log.e(TAG, "button clicked to cart ${products.get(position).brandName}")
                response = viewModel.checkItemInCart(productCode)
                buttonState = response != Response.NO_SUCH_ITEM_IN_CART
                toggleAddToCartButton(buttonState, addToCartButton)

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

    private fun toggleAddToCartButton(state:Boolean, addToCartButton:ImageView){
        if (state) {
            addToCartButton.setColorFilter(Color.argb(255, 255, 255, 255));
            addToCartButton.setBackgroundResource(R.drawable.product_list_add_to_cart_icon_clicked_bg)
        } else {
            addToCartButton.setColorFilter(Color.argb(255, 164, 191, 246));

            addToCartButton.setBackgroundResource(R.drawable.product_list_add_to_cart_icon_bg)
        }
    }

}