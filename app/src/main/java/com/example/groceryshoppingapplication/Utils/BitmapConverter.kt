package com.example.groceryshoppingapplication.Utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil.Companion.assetManager
import com.example.groceryshoppingapplication.Utils.BitmapConverter.categImgHome
import java.io.IOException
import java.io.InputStream
//
object BitmapConverter {
    val categImgHome = AssetManagerUtil.assetManager.list("category_images")
    private val assetManager = AssetManagerUtil.assetManager


    fun getBitmapFromAsset(
        productCode: String,
        position: Int
    ): Bitmap? {
            val imgPath = assetManager.list("product_images/$productCode")?.get(position)
            var istr: InputStream? = null
            try {
                istr = assetManager.open("product_images/$productCode/$imgPath")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(ContentValues.TAG, "IO EXCEPTION IN GETBITMAP METHOD")
            }
            return BitmapFactory.decodeStream(istr)

    }

    fun getBitmapForCategory(index: Int): Bitmap? {
        val imgPath = categImgHome!!.get(index)
        var istr: InputStream? = null
        try {
            istr = assetManager.open("category_images/$imgPath")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(ContentValues.TAG, "IO EXCEPTION IN GETBITMAP METHOD")
        }
        return BitmapFactory.decodeStream(istr)

    }


}
