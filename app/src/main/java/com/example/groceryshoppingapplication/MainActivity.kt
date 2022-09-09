package com.example.groceryshoppingapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        val fragment = LoginScreenFragment()
        fm.beginTransaction().apply {
            add(R.id.main_fragment_container, fragment).commit()

        }




    }
}