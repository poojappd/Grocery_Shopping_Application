package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.Response

class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val signInFragmentView = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val mobileNumberInput = signInFragmentView.findViewById<EditText>(R.id.mobile_number_input_field)

        signInFragmentView.findViewById<Button>(R.id.signInButton2).setOnClickListener {
            val mobileNumberCheckResponse = checkMobileNumber(mobileNumberInput)

            if (mobileNumberCheckResponse == Response.MOBILE_NUMBER_VALID) {
                OtpDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    "otp fragment tag"
                )

            }
            else {
                Toast.makeText(context, mobileNumberCheckResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
        return signInFragmentView


    }

    private fun checkMobileNumber(mobileNumberInputField: EditText): Response {

        return when {
            mobileNumberInputField.text.length < 10 -> Response.MOBILE_NUMBER_LENGTH_SHORT
            mobileNumberInputField.text[0].digitToInt() in 0..5 -> Response.MOBILE_NUMBER_NOT_VALID
            else -> Response.MOBILE_NUMBER_VALID
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG,"ONRESUME SIGIN (MOBILE)")

    }
}
