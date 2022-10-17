package com.example.groceryshoppingapplication.Utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException
import java.io.InputStream
//
object BitmapFactory {
    val categImgHome = AssetManagerUtil.assetManager.list("category_images")
    private val adimages = AssetManagerUtil.assetManager.list("advertisement_images")

    private val assetManager = AssetManagerUtil.assetManager


    fun getProductBitmapFromAsset(
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

    fun getCategoryBitmapFromAsset(index: Int): Bitmap? {
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


    fun getAdvertisementBitmapFromAsset(): MutableList<Bitmap> {
        val imageList = mutableListOf<Bitmap>()
        for (i in adimages?.indices!!) {
            val imgPath = adimages.get(i)
            var istr: InputStream? = null
            try {
                istr = assetManager.open("advertisement_images/$imgPath")
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(ContentValues.TAG, "IO EXCEPTION IN GETBITMAP METHOD")
            }
            imageList.add( BitmapFactory.decodeStream(istr))
        }
        return imageList

    }
}
