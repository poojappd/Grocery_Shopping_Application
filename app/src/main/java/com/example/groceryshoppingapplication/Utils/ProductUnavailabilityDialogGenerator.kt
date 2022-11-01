package com.example.groceryshoppingapplication.Utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.UnavailableItemsAdapter
import com.example.groceryshoppingapplication.enums.OrderStatus
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.button
import kotlinx.android.synthetic.main.product_not_available_dialog_layout.view.*

class ProductUnavailabilityDialogGenerator(private val layoutInflater: LayoutInflater,private val context: Context, private val isSingleProduct:Boolean,private val productTitles:List<String>? = null, private val productCodes:List<String>?=null) {
    fun buildDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView =
            layoutInflater.inflate(R.layout.product_not_available_dialog_layout, null)

        dialogBuilder.setView(dialogView)
        val dialogMessage = dialogView.dialog_message_product
        val yesButtonDialog = dialogView.button
        val alertDialog = dialogBuilder.create()
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow()!!
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }

        dialogMessage.text = if(isSingleProduct) {
            dialogView.productRecyclerVIew.visibility = View.GONE

            "This product is out of stock/ in limited quantity and hence been reduced from your cart"
        }
        else{
            val recyclerView = dialogView.productRecyclerVIew
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = UnavailableItemsAdapter(productCodes!!, productTitles!!)
            StringBuilder().append("The following products are in limited quantity/out of stock, Hence they have been reduced from your cart")

        }
        yesButtonDialog.setOnClickListener { view ->
            alertDialog.cancel()
        }
        return alertDialog
    }
}