package com.example.groceryshoppingapplication

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.groceryshoppingapplication.Utils.AssetManagerUtil
import com.example.groceryshoppingapplication.fragments.LoginScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AssetManagerUtil().setAssetManager(applicationContext)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        //setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homePageFragment -> {navController.navigate(R.id.homePageFragment)}
                R.id.allCategoriesFragment -> navController.navigate(R.id.allCategoriesFragment)
                else -> navController.navigate(R.id.cartFragment)
            }
            return@setOnItemSelectedListener true
        }


                val sharedPref = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//        sharedPref.edit().apply {
//            clear()
//            apply()
//        }
        if (sharedPref.getString("loggedUserMobile", null) != null){
            navController.navigate(R.id.homePageFragment)
        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp()
    }

}