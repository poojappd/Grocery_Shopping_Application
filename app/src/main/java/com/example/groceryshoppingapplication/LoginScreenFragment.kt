package com.example.groceryshoppingapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
                        refActivity.supportFragmentManager.popBackStack()
                        add(R.id.main_fragment_container, SignInFragment())
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