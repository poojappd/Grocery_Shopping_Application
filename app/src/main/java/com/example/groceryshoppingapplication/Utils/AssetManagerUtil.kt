package com.example.groceryshoppingapplication.Utils

import android.content.Context
import android.content.res.AssetManager

class AssetManagerUtil {
    companion object{
        lateinit var assetManager:AssetManager
    }
    fun setAssetManager(context: Context){
        assetManager = context.assets
    }
}