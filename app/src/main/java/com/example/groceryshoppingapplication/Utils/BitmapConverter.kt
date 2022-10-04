package com.example.groceryshoppingapplication.Utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Index
import java.io.IOException
import java.io.InputStream
//
object BitmapConverter {
    val categImgHome = AssetManagerUtil.assetManager.list("category_images")

    fun getBitmapFromAsset(
        productCode: String,
        ifSingleImage: Boolean,
        path: String? = null
    ): Bitmap? {
        val assetManager = AssetManagerUtil.assetManager
        val path = if (ifSingleImage) "1.webp" else path
        var istr: InputStream? = null
        try {
            istr = assetManager.open("product_images/$productCode/$path")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(ContentValues.TAG, "IO EXCEPTION IN GETBITMAP METHOD")
        }
        return BitmapFactory.decodeStream(istr)
    }

    fun getBitmapFromImageAsset(index: Int): Bitmap? {
        val imgPath = categImgHome!!.get(index)
        val assetManager = AssetManagerUtil.assetManager
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
