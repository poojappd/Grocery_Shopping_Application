package com.example.groceryshoppingapplication.Utils

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.Glide
import java.io.IOException
import java.io.InputStream
//
object BitmapFactory  {
    val categImgHome = AssetManagerUtil.assetManager.list("category_images")
    private val adimages = AssetManagerUtil.assetManager.list("advertisement_images")

    private val assetManager = AssetManagerUtil.assetManager



    fun getProductImageUri(
        productCode: String,
        position: Int
    ): String {
        val imgPath = assetManager.list("product_images/$productCode")?.get(position)
        return "file:///android_asset/product_images/$productCode/$imgPath"
    }

    fun getProductBitmapFromAsset(
        productCode: String,
        position: Int
    ): Bitmap? {
            val imgPath = assetManager.list("product_images/$productCode")?.get(position)
        Log.e(TAG, imgPath.toString())
            var istr: InputStream? = null
            try {
                istr = assetManager.open("product_images/$productCode/$imgPath")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(ContentValues.TAG, "IO EXCEPTION IN GETBITMAP METHOD")
            }
            return BitmapFactory.decodeStream(istr)

    }

    fun getCategoryBitmapPathFromAsset(index: Int): String {
        val imgPath = categImgHome!!.get(index)
        return "file:///android_asset/category_images/$imgPath"
    }



    fun getAdvertisementBitmapFromAsset(): MutableList<String> {
        val imageList = mutableListOf<String>()
        for (i in adimages?.indices!!) {
            val imgPath = adimages.get(i)
            imageList.add("file:///android_asset/advertisement_images/$imgPath")
        }
        return imageList

    }
}
