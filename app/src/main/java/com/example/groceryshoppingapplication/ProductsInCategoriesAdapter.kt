package com.example.groceryshoppingapplication

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
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.fragments.ProductsListFragmentDirections
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import kotlinx.android.synthetic.main.single_product_row_item_in_list.view.*
import java.io.IOException
import java.io.InputStream

class ProductsInCategoriesAdapter(private val products: List<GroceryItemEntity>,private val context: Context, private val navController: NavController
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
            Log.e(TAG, "position - ${products.get(position).brandName}  ${buttonClickedStates.get(products.get(position).productCode)}")

            image.setImageBitmap(
                getBitmapFromAsset(products.get(position).productCode.toString())
            )
            container.setOnClickListener{
                val productCode = products.get(position).productCode
                val action = ProductsListFragmentDirections.actionProductsListFragmentToSingleProductViewFragment(productCode)
                navController.navigate(action)
            }
            addToCartButton.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(context,R.anim.add_to_cart_icon_in_product_animation))
                val productCode = products.get(position).productCode
                Log.e(TAG,"button clicked to cart ${products.get(position).brandName}")



                val state = buttonClickedStates.get(productCode)
                if (state == null || state == false) {
                    addToCartButton.setColorFilter(Color.argb(255, 255, 255, 255));
                    it.setBackgroundResource(R.drawable.product_list_add_to_cart_icon_clicked_bg)
                    buttonClickedStates.put(productCode, true)
                } else {
                    addToCartButton.setColorFilter(Color.argb(255, 164, 191, 246));

                    it.setBackgroundResource(R.drawable.product_list_add_to_cart_icon_bg)
                    buttonClickedStates.put(productCode, false)
                }
            }

        }

    }



override fun getItemCount(): Int {
    return products.size
}


private fun getBitmapFromAsset(productCode: String): Bitmap? {
    val assetManager = AssetManagerUtil.assetManager
    var istr: InputStream? = null
    try {
        istr = assetManager.open("product_images/$productCode/1.webp")
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e(TAG, "IO EXCEPTION IN GETBITMAP METHOD")
    }
    return BitmapFactory.decodeStream(istr)
}
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}