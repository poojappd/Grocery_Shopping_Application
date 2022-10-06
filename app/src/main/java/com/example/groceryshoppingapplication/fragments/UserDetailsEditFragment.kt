package com.example.groceryshoppingapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_details_edit.view.*
import java.text.SimpleDateFormat
import java.util.*


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
        userViewModel.currentUser.observe(viewLifecycleOwner) {
            view.textView10.setText(it.firstName)
            view.textView13.setText(it.mobileNumber)
            view.textView13.setOnClickListener {

            }
            it.lastName?.let { lastName ->
                view.textView11.setText(lastName)
            } ?: run {
                view.textView11.text?.clear()
            }
            it.dob?.let { dob ->
                view.textView15.text = dob
//            } ?: run {
//                val calendar = Calendar.getInstance()
//                val day = calendar.get(Calendar.DAY_OF_MONTH)
//                val month = calendar.get(Calendar.MONTH)
//                val year = calendar.get(Calendar.YEAR)
//                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
//                val datePickerDialog = DatePickerDialog(
//                    this,
//                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                        view.textView15.text = dateFormat.
//                    })
//            }

            }

        }

        return view
    }
}