package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.models.WishListEntity
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlin.math.sign

class SignInFragment(private val signingMode: Boolean,private val signUpListener:(Boolean)->Unit) : Fragment() {

    val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val signInFragmentView = inflater.inflate(R.layout.fragment_sign_in, container, false)
        signInFragmentView.isClickable = true
        if (!signingMode)
            signInFragmentView.signInButton2.setText("Sign Up")
        val mobileNumberInput =
            signInFragmentView.findViewById<EditText>(R.id.mobile_number_input_field)

        val invalidMobileToast =
            Toast.makeText(context, Response.MOBILE_NUMBER_NOT_VALID.message, Toast.LENGTH_SHORT)
        val mobileNumNotEnteredToast =
            Toast.makeText(context, Response.MOBILE_NUMBER_NOT_ENTERED.message, Toast.LENGTH_SHORT)
        val mobileNumShortToast =
            Toast.makeText(context, Response.MOBILE_NUMBER_LENGTH_SHORT.message, Toast.LENGTH_SHORT)

        signInFragmentView.findViewById<Button>(R.id.signInButton2).setOnClickListener {
            val sharedPref =
                requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

            val mobileNumberCheckResponse = checkMobileNumber(mobileNumberInput)

            if (mobileNumberCheckResponse == Response.MOBILE_NUMBER_VALID) {
                if (!signingMode) {//sign up

                    val mobileNum = mobileNumberInput.text.toString()
                    val loginResponse = userViewModel.loginUser(mobileNum)

                    if (loginResponse == Response.NO_SUCH_USER) {
                        val newUserId = CodeGeneratorUtil.generateUserId()
                        val newUserCartId = CodeGeneratorUtil.generateCartId()
                        val newUserWishListId = CodeGeneratorUtil.generateWishListId()
                        val newUser = User(newUserId, mobileNum)
                        userViewModel.createUser(
                            newUser,
                            CartEntity(newUserId, newUserCartId),
                            WishListEntity(newUserId, newUserWishListId)
                        )
                        sharedPref.edit().apply {
                            putString("loggedUserMobile", mobileNum)
                            putInt("loggedUserCartId", newUserCartId)
                            putString("loggedUserId", newUserId)
                            apply()
                        }
                        Log.e(
                            TAG, "loggedUserMobile - signup  " +
                                    userViewModel.currentUser.value
                        )
                        OtpDialogFragment(signingMode).show(
                            requireActivity().supportFragmentManager,
                            "otp fragment tag"
                        )

                    } else {
                        Toast.makeText(
                            this.context,
                            "Account already exists!\n Sign In to continue",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                } else {//signIn

                    signInFragmentView.invisible_signUpButton.setOnClickListener {
                        signUpListener(true)
                    }
                    if (userViewModel.loginUser(mobileNumberInput.text.toString()) == Response.NO_SUCH_USER) {
                        Toast.makeText(
                            this.context,
                            "No users found!\nKindly Sign Up to continue",
                            Toast.LENGTH_SHORT
                        ).show()

                    signInFragmentView.invisible_signUp_layout.isVisible = true
                    } else {
                        sharedPref.edit().apply {
                            putString("loggedUserMobile", mobileNumberInput.text.toString())
                            putString("loggedUserId", userViewModel.currentUser.value!!.userId)
                            putInt("loggedUserCartId", userViewModel.currentUserCart.value!!.cartId)

                            apply()
                        }
                        Log.e(
                            TAG, "loggedUserMobile singIN" +
                                    userViewModel.currentUser.value
                        )
                        OtpDialogFragment(true).show(
                            requireActivity().supportFragmentManager,
                            "otp fragment tag"
                        )


                    }

                }

            } else {
                if (mobileNumberInput.text.toString().length == 0) {
                    mobileNumNotEnteredToast.show()
                }
                else if(mobileNumberCheckResponse == Response.MOBILE_NUMBER_LENGTH_SHORT)
                    mobileNumShortToast.show()
                else
                    invalidMobileToast.show()
            }
        }
        return signInFragmentView


    }

    private fun checkMobileNumber(mobileNumberInputField: EditText): Response {

        return when {
            mobileNumberInputField.text.length < 10 -> Response.MOBILE_NUMBER_LENGTH_SHORT
            mobileNumberInputField.text[0].digitToInt() in 0..4 -> Response.MOBILE_NUMBER_NOT_VALID
            else -> Response.MOBILE_NUMBER_VALID
        }
    }

}
