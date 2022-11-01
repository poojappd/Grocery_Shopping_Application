package com.example.groceryshoppingapplication.Utils

import android.app.Activity
import android.content.Context
import android.widget.Toast




class ToastMessageProvider(val context: Context?) {
    private var mToast: Toast? = null

    fun show(text: String?) {
            mToast?.cancel()
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            mToast!!.show()
        }

}