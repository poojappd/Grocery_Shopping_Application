package com.example.groceryshoppingapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication.Companion.cartId
import com.example.groceryshoppingapplication.data.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AssetManagerUtil().setAssetManager(applicationContext)
        CodeGeneratorUtil.setIdDao(AppDatabase.getDatabase(applicationContext).getIdsDao())
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        //setupWithNavController(bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            //Log.e(TAG, destination.toString())
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
                else -> bottomNavigationView.menu.findItem(R.id.cartFragment).setChecked(true)
            }
        }
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homePageFragment -> navController.navigate(R.id.homePageFragment)
                R.id.allCategoriesFragment -> navController.navigate(R.id.allCategoriesFragment)
                R.id.userAccountFragment -> navController.navigate(R.id.userAccountFragment)
                R.id.productSearchFragment -> navController.navigate(R.id.productSearchFragment)
                else -> navController.navigate(R.id.cartFragment)
            }
            return@setOnItemSelectedListener true
        }

        val bottomNavCartMenu = bottomNavigationView.getChildAt(4)


    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp()
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "MAIN ACTIVITY ON CREATE")
        Log.e(TAG, "APP ON CREATE -- $cartId")



        cartId.observe(this){

            AppDatabase.getDatabase(applicationContext).getCartDao().getCartItemCount(it).observe(this)
            {
                if (it != null && it > 0) {
                    val badge = bottomNavigationView.getOrCreateBadge(R.id.cartFragment)
                    badge.backgroundColor = Color.parseColor("#7584e7")
                    badge.number = it
                } else
                    bottomNavigationView.removeBadge(R.id.cartFragment)
            }
        }



    }


}