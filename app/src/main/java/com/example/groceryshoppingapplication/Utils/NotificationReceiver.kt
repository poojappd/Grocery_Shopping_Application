package com.example.groceryshoppingapplication.Utils

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.groceryshoppingapplication.R
import java.util.*

class NotificationReceiver: BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG,"RECEIVED...")
         context?.let {
             val date = intent?.extras?.getString("deliveryTime")
             Log.e(TAG,"RECEIVED context...")
             val builder = NotificationCompat.Builder(it,"groceroChannel").apply {
            setSmallIcon(R.drawable.app_cart_icon)
            setContentTitle("Arriving Today @${if( date=="" ) Date() else date }")
                 setColor(Color.argb(100,101,172,255))
            setContentText("Your delivery is scheduled for today @$date")
            setPriority(NotificationCompat.PRIORITY_HIGH)

        }
            NotificationManagerCompat.from(context).notify(200,builder.build() )

        }
    }
}