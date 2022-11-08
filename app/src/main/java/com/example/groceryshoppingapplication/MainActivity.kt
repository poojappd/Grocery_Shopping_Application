package com.example.groceryshoppingapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication.Companion.cartId
import com.example.groceryshoppingapplication.data.AppDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appDatabase: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)
        AssetManagerUtil().setAssetManager(applicationContext)
        CodeGeneratorUtil.setIdDao(AppDatabase.getDatabase(applicationContext).getIdsDao())
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        //setupWithNavController(bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.e(TAG,"----------------ENTRIES---------------------------")
            controller.backQueue.forEach {
                it.id
                Log.e(TAG,it.destination.toString())
            }
            Log.e(TAG,"------------------------------------------------------")
            when (destination.id) {
                R.id.userAccountFragment -> bottomNavigationView.menu.findItem(R.id.userAccountFragment)
                    .setChecked(true)
                R.id.homePageFragment -> bottomNavigationView.menu.findItem(R.id.homePageFragment)
                    .setChecked(true)
                R.id.allCategoriesFragment -> bottomNavigationView.menu.findItem(R.id.allCategoriesFragment)
                    .setChecked(true)
                R.id.productsListFragment -> bottomNavigationView.menu.findItem(R.id.allCategoriesFragment)
                    .setChecked(true)
                R.id.productSearchFragment -> bottomNavigationView.menu.findItem(R.id.productSearchFragment)
                    .setChecked(true)
                R.id.wishListFragment -> bottomNavigationView.menu.findItem(R.id.userAccountFragment)
                    .setChecked(true)
                else -> bottomNavigationView.menu.findItem(R.id.cartFragment).setChecked(true)
            }
        }
        bottomNavigationView.setOnItemSelectedListener {
            if(it.itemId!=navController.currentDestination?.id) {
                when (it.itemId) {
                    R.id.homePageFragment -> navController.navigate(R.id.homePageFragment)
                    R.id.allCategoriesFragment -> navController.navigate(R.id.allCategoriesFragment)
                    R.id.userAccountFragment -> navController.navigate(R.id.userAccountFragment)
                    R.id.productSearchFragment -> navController.navigate(R.id.productSearchFragment)
                    else -> {
                        MyGroceryApplication.modifiedStateEnabled.value?.let {
                            if (it == true)
                                navController.navigate(R.id.modifyOrderFragment)
                            else
                                navController.navigate(R.id.cartFragment)
                        } ?: run {
                            navController.navigate(R.id.cartFragment)
                        }
                    }
                }
                return@setOnItemSelectedListener true
            }
            return@setOnItemSelectedListener false
        }

        val bottomNavCartMenu = bottomNavigationView.getChildAt(4)
        appDatabase = AppDatabase.getDatabase(applicationContext)
        val ordersDao = appDatabase.getOrdersDao()
        lifecycleScope.launch {
            ordersDao.clearTableModifiedItems()
            ordersDao.clearTableModifiedOrder()
        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp()
    }

    override fun onResume() {
        super.onResume()

        Log.e(TAG, "MAIN ACTIVITY ON CREATE")
        Log.e(TAG, "APP ON CREATE -- $cartId")

        cartId.observe(this) {

            appDatabase.getCartDao().getCartItemCount(it).observe(this)
            {
                if (it != null && it > 0) {
                    val badge = bottomNavigationView.getOrCreateBadge(R.id.cartFragment)
                    badge.backgroundColor = Color.parseColor("#7584e7")
                    badge.number = it
                } else
                    bottomNavigationView.removeBadge(R.id.cartFragment)
            }
        }
        var snackBar = Snackbar.make(
            snackBarView,
            "You are now modifying your order, items are saved to your cart",
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Got it") {
            snackBar.dismiss()
        }

        MyGroceryApplication.modifiedStateEnabled.observe(this) {
            Log.e(TAG,"----------------------------")

            Log.e(TAG,"state: $it, orderID:${MyGroceryApplication.modifiedStateOrderId.value.toString()}")

            Log.e(TAG,"----------------------------")

            if(it)
            {
                snackBar.show()

            }
            else{
                snackBar.dismiss()
                snackBar = Snackbar.make(
                    snackBarView,
                    "You are now modifying your order, items are saved to your cart",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction("Got it") {
                    snackBar.dismiss()
                }
            }

        }


    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Delivery Reminders"
            Log.e(TAG,"$name NOtification creation processin. . .")

            val description = "This is a  notificatio channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("groceroChannel", name, importance)
            channel.description = description
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        }
        else
            Log.e(TAG,"Build problem hey")

    }


}