package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.groceryshoppingapplication.R

class LoginScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginScreenFragmentView = inflater.inflate(R.layout.fragment_login_screen, container, false)
        val signInButton = loginScreenFragmentView.findViewById<Button>(R.id.signInButton)
        val refActivity = activity


        signInButton.setOnClickListener{
                refActivity?.supportFragmentManager?.apply {
                    beginTransaction().apply {
                        setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        add(R.id.signIn_up_frg_cont, SignInFragment())
                        addToBackStack("one")
                        Log.e(
                            ContentValues.TAG,
                            refActivity.supportFragmentManager.backStackEntryCount.toString()
                        )

                        commit()
                    }
                }

        }

        val signUpButtn = loginScreenFragmentView.findViewById<Button>(R.id.signUpButton).setOnClickListener {

        }
        return loginScreenFragmentView

    }



}