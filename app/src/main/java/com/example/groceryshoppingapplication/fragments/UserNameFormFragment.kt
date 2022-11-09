package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.ValidationService
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_snackbar_layout.view.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*
import kotlinx.android.synthetic.main.fragment_username_form.view.*


class UserNameFormFragment : Fragment() {
    val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val userFromView = inflater.inflate(R.layout.fragment_username_form, container, false)
        val snackBarCustomLayout = layoutInflater.inflate(R.layout.error_snackbar_layout, null)
        var snackBar = Snackbar.make(
            userFromView.userName_RootView,
            "",
            5000
        )
        snackBar.getView().setBackgroundColor(Color.TRANSPARENT)
        val view: View = snackBar.getView()
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        val snackbarLayout = snackBar.getView() as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(snackBarCustomLayout, 0);
        snackbarLayout.snackBarDismissButton.setOnClickListener {
            snackBar.dismiss()
        }
        userFromView.apply {
            fname_signUp_et.doOnTextChanged { text, start, before, count ->
                if (fname_signUp.isErrorEnabled) {
                    fname_signUp.setErrorEnabled(false)
                }
            }
            lName_signUp_et.doOnTextChanged { text, start, before, count ->
                lName_signUp.setErrorEnabled(false)
            }

            proceedButton_signUp.setOnClickListener {
                val firstName = fname_signUp_et
                if (TextUtils.isEmpty(firstName.text) || firstName.text.toString().trim() == "") {
                    fname_signUp.setError("First name is required!")
                    firstName.requestFocus()

                } else if (firstName.text.toString().trim().length < 3) {
                    fname_signUp.setError("First name is too short!")
                    firstName.requestFocus()
                } else if (!ValidationService.validateFirstName(firstName.text.toString().trim())) {
                    fname_signUp.setError("Enter a valid first name")
                    snackBar.show()
                    firstName.requestFocus()
                } else if (!TextUtils.isEmpty(lName_signUp_et.text) && lName_signUp_et.text.toString()
                        .trim() != "" && !ValidationService.validateLastName(lName_signUp_et.text.toString().trim())
                ) {
                    lName_signUp.setError("Enter a valid last name")
                    lName_signUp.requestFocus()
                    snackBar.show()

                } else {
                    userViewModel.currentUser.value?.firstName = firstName.text.toString()
                    userViewModel.currentUser.value?.let { it1 ->
                        userViewModel.updateUser(
                            it1
                        )
                    }
                    findNavController().navigate(R.id.action_userNameFormFragment_to_homePageFragment)
                }
            }
        }
        return userFromView
    }
}