package com.example.groceryshoppingapplication.fragments

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_details_edit.view.*
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*
import java.lang.StringBuilder
import java.util.*
import com.example.groceryshoppingapplication.Utils.ValidationService

class UserDetailsEditFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_details_edit, container, false)
        val firstNameEditText = view.textView10
        val lastNameEditText = view.textView11
        val mobileNumberEditText = view.textView13
        val dateTextView = view.textView15
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.mobilenumber_chage_alert_layout, null)
        dialogBuilder.setView(dialogView)
        val yesButtonDialog = dialogView.button
        val noButtonDialog = dialogView.no
        var mobileNumberChangeInvoked = false
        var pressedYes = false

        val alertDialog = dialogBuilder.create()
        yesButtonDialog.setOnClickListener {
            mobileNumberChangeInvoked = true
            pressedYes = true


            alertDialog.cancel()
            mobileNumberEditText.isFocusableInTouchMode = true
            mobileNumberEditText.requestFocus()
            mobileNumberEditText.setSelection(1)
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(mobileNumberEditText, InputMethodManager.SHOW_IMPLICIT);
        }
        noButtonDialog.setOnClickListener {
            alertDialog.cancel()
        }


        userViewModel.currentUser.observe(viewLifecycleOwner) {
            firstNameEditText.setText(it.firstName)
            mobileNumberEditText.setText(it.mobileNumber)
            it.lastName?.let { lastName ->
                lastNameEditText.setText(lastName)
            } ?: run {
                lastNameEditText.text?.clear()
            }
            it.dob?.let { dob ->
                dateTextView.text = dob
            }

        }

        dateTextView.setOnClickListener{
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { view, year, month, dayOfMonth ->
                    dateTextView.setText(StringBuilder().append("$dayOfMonth/${month+1}/$year").toString())
                }, currentYear, currentMonth, day)
            calendar.add(Calendar.YEAR, -120)

            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            calendar.add(Calendar.YEAR,102)
            datePickerDialog.datePicker.maxDate =calendar.timeInMillis

            datePickerDialog.show()
        }

        mobileNumberEditText.setOnClickListener {
            if(!mobileNumberChangeInvoked) {
                alertDialog.show()
                Log.e(TAG,"pressed yes? "+pressedYes)

            }
        }

        view.saveButton.setOnClickListener {
            val firstNameValid = ValidationService.validateName(firstNameEditText.text.toString())
            val lastNameValid = if (lastNameEditText.text.toString().trim() != "" || lastNameEditText.text.toString().trim()!=" ") ValidationService.validateName(lastNameEditText.text.toString()) else true

            if(!firstNameValid){
                firstNameEditText.setError("First name is invalid")
            }
            userViewModel.currentUser.observe(viewLifecycleOwner){
                userViewModel.updateUser(
                it.apply {
                    firstName = firstNameEditText.text.toString()
                    lastName = lastNameEditText.text.toString()
                    dob = dateTextView.text.toString()
                    mobileNumber = mobileNumberEditText.text.toString()

                }
                )
                findNavController().popBackStack()
            }
        }

        return view
    }
}