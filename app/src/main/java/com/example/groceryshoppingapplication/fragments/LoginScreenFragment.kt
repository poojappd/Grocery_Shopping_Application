package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.SharedPrefViewModel
import com.example.groceryshoppingapplication.SharedPrefViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory

class LoginScreenFragment : Fragment() {

    val viewModel: SharedPrefViewModel by activityViewModels {
        SharedPrefViewModelFactory(requireActivity().application)
    }
    val userViewModel:UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val loginScreenFragmentView = inflater.inflate(R.layout.fragment_login_screen, container, false)
        val signInButton = loginScreenFragmentView.findViewById<Button>(R.id.signInButton)
        val refActivity = activity

        Log.e(TAG, viewModel.userMobile.toString())
        if (viewModel.userMobile != null){
            userViewModel.loginUser(viewModel.userMobile.toString())
            Log.e(TAG,userViewModel.currentUser.value.toString())
            findNavController().navigate(R.id.homePageFragment)
        }


        signInButton.setOnClickListener{
                refActivity?.supportFragmentManager?.apply {
                    beginTransaction().apply {
                        setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        add(R.id.signIn_up_frg_cont, SignInFragment(true))
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
            refActivity?.supportFragmentManager?.apply {
                beginTransaction().apply {
                    setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    add(R.id.signIn_up_frg_cont, SignInFragment(false))
                    addToBackStack("one")
                    Log.e(
                        ContentValues.TAG,
                        refActivity.supportFragmentManager.backStackEntryCount.toString()
                    )

                    commit()
                }
            }
        }
        return loginScreenFragmentView

    }



}