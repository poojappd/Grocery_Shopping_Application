package com.example.groceryshoppingapplication.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
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
import com.example.groceryshoppingapplication.enums.Response
import kotlinx.android.synthetic.main.fragment_edit_address.view.*

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
        val firstNameEditText = view.textView10_et
        val lastNameEditText = view.textView11_et
        val mobileNumberEditText = view.textView13_et
        val dateTextView = view.textView15
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.mobilenumber_chage_alert_layout, null)
        dialogBuilder.setView(dialogView)
        val yesButtonDialog = dialogView.button
        val noButtonDialog = dialogView.no
        var mobileNumberChangeInvoked = false
        var pressedYes = false

        val alertDialog = dialogBuilder.create()
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
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

            }
        }

        view.saveButton.setOnClickListener {
            var validationPassed = true
            val fname = firstNameEditText.text
            if(TextUtils.isEmpty(fname) || fname.toString().trim() == ""){
                    view.textView10.setError(Response.FIELD_REQUIRED.message)
                    view.textView10.requestFocus()
                    validationPassed = false
                }
            else {
                    if (!ValidationService.validateFirstName(fname.toString().trim())) {
                        view.textView10.setError(Response.FNAME_INVALID.message)
                        view.textView10.requestFocus()
                        validationPassed = false
                    }
                }

            val lname =lastNameEditText.text

            if(!TextUtils.isEmpty(lname) || lname.toString().trim() != ""){
                if (!ValidationService.validateLastName(lname.toString().trim())) {
                    view.textView11.setError(Response.LNAME_INVALID.message)
                    view.textView11.requestFocus()
                    validationPassed = false
                }
            }

           if(mobileNumberChangeInvoked){
               if(TextUtils.isEmpty(mobileNumberEditText.text)){
                   view.textView13.setError(Response.FIELD_REQUIRED.message)
                   view.textView13.requestFocus()
                   validationPassed = false
               }

                else if(!ValidationService.validateMobileNumber(mobileNumberEditText.text.toString())){
                    view.textView13.setError(Response.MOBILE_NUMBER_LENGTH_SHORT.message)
                    view.textView13.requestFocus()
                    validationPassed = false
                }
           }

            if(validationPassed) {
                userViewModel.currentUser.observe(viewLifecycleOwner) {
                    userViewModel.updateUser(
                        it.apply {
                            firstName = firstNameEditText.text.toString().trim()
                            lastName =  if (lname!=null || lname?.trim()=="") lname.toString().trim() else null
                            dob = dateTextView.text.toString()
                            mobileNumber = mobileNumberEditText.text.toString()

                        }
                    )
                    findNavController().popBackStack()
                }
            }
        }

        return view
    }
}