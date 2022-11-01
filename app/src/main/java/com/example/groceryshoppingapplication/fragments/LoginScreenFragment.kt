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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.view.*

class LoginScreenFragment : Fragment() {


    val userViewModel:UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val loginScreenFragmentView = inflater.inflate(R.layout.fragment_login_screen, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE
        val signInButton = loginScreenFragmentView.findViewById<Button>(R.id.signInButton)
        val refActivity = activity
        val sharedPref = MyGroceryApplication.preferences
        val userMobile = sharedPref.getString("loggedUserMobile",null)
        if (userMobile != null){
            userViewModel.loginUser(userMobile.toString())
            findNavController().navigate(R.id.action_loginScreenFragment_to_homePageFragment)
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
                        add(R.id.signIn_up_frg_cont, SignInFragment(true){signUp:Boolean-> if(signUp) skipToSignup()})
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
                    add(R.id.signIn_up_frg_cont, SignInFragment(false){})
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

    fun skipToSignup(){
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.apply {
            beginTransaction().apply {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                add(R.id.signIn_up_frg_cont, SignInFragment(false){signUp:Boolean-> if(signUp) skipToSignup()})
                addToBackStack("one")

                Log.e(
                    ContentValues.TAG,
                    requireActivity().supportFragmentManager.backStackEntryCount.toString()
                )
               commit()

            }
        }
    }



}