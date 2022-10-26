package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.ValidationService
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
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
        userFromView.apply {
            proceedButton_signUp.setOnClickListener {
                val firstName = this.fname_signUp
                if (TextUtils.isEmpty(firstName.text)) {
                    firstName.setError("First name is required!")
                    firstName.requestFocus()

                } else if (firstName.text.toString().length < 3) {
                    firstName.setError("First name is too short!")
                    firstName.requestFocus()
                } else if (!ValidationService.validateFirstName(firstName.text.toString())) {
                    firstName.setError("Enter a valid first name")
                    firstName.requestFocus()
                } else if (!TextUtils.isEmpty(lName_signUp.text) && !ValidationService.validateFirstName(lName_signUp.text.toString())) {
                    lName_signUp.setError("Enter a valid last name")
                        lName_signUp.requestFocus()

                } else {
                    userViewModel.currentUser.value?.firstName = firstName.text.toString()
                    userViewModel.currentUser.value?.let { it1 ->
                        userViewModel.updateUser(
                            it1
                        )
                    }
                    findNavController().navigate(R.id.homePageFragment)
                }
            }
        }
        return userFromView
    }
}