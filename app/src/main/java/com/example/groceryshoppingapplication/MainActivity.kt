package com.example.groceryshoppingapplication

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.data.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AssetManagerUtil().setAssetManager(applicationContext)
        CodeGeneratorUtil.setIdDao(AppDatabase.getDatabase(applicationContext).getIdsDao())
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        //setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homePageFragment -> {navController.navigate(R.id.homePageFragment)
                }
                R.id.allCategoriesFragment -> navController.navigate(R.id.allCategoriesFragment)
                R.id.userAccountFragment -> navController.navigate(R.id.userAccountFragment)
                R.id.productSearchFragment -> navController.navigate(R.id.productSearchFragment)
                else -> navController.navigate(R.id.cartFragment)
            }
            return@setOnItemSelectedListener true
        }

        val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val currentCartId = sharedPref.getInt("loggedUserCartId",-1)
        AppDatabase.getDatabase(applicationContext).getCartDao().getCartItemCount(currentCartId).observe(this){
            if(it>0) {
                val badge = bottomNavigationView.getOrCreateBadge(R.id.cartFragment)
                badge.backgroundColor = Color.parseColor("#7584e7")
                badge.number = it
            }
            else
                bottomNavigationView.removeBadge(R.id.cartFragment)

        }
        val bottomNavCartMenu = bottomNavigationView.getChildAt(4)


    }
    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp()
    }





}