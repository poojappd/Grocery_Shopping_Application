package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_account.view.*
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*

class UserAccountFragment : Fragment() {
    val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)

        val currentUser = userViewmodel.currentUser.value!!
        val userAddresses = userViewmodel.currentUserAddresses.value!!
        val currentUserAddress = if (userAddresses.isEmpty()) null else userAddresses.get(0)

        val userFullName = ((currentUser.firstName) ?: "-") + (currentUser.lastName ?: "")
        view.textView20.text = userFullName
        view.textView21.text = currentUser.mobileNumber
        currentUserAddress?.let {
            view.textView9.text = it.areaDetails
            view.cityPincode.text = StringBuilder().append(it.city + " - " + it.pincode)
        } ?: run {
            view.textView9.text = "-"
            view.cityPincode.text = ""
        }
        view.textView17.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_addressesFragment)
        }
        view.EditButton.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_userDetailsEditFragment)
        }

        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.mobilenumber_chage_alert_layout, null)
        dialogBuilder.setView(dialogView)
        val dialogMessage = dialogView.dialog_message
        val yesButtonDialog = dialogView.button
        val noButtonDialog = dialogView.no

        val alertDialog = dialogBuilder.create()
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialogMessage.text = StringBuilder().append("Are you sure you want to Logout?")
        yesButtonDialog.setOnClickListener {
            val sharedPref = MyGroceryApplication.preferences
            sharedPref.edit().apply {
                clear()
                apply()
            }
            alertDialog.cancel()

            popBackStackAndGoToLoginPage()
        }
        noButtonDialog.setOnClickListener {
            alertDialog.cancel()
        }

        val deleteAccountDialogBuilder = AlertDialog.Builder(requireContext())
        val deleteAccountDialogView =
            layoutInflater.inflate(R.layout.mobilenumber_chage_alert_layout, null)
        deleteAccountDialogBuilder.setView(deleteAccountDialogView)
        val deleteAccountDialogMessage = deleteAccountDialogView.dialog_message
        val deletedAccountYesButtonDialog = deleteAccountDialogView.button
        val deleteAccountNoButtonDialog = deleteAccountDialogView.no

        val deleteAccountAlertDialog = deleteAccountDialogBuilder.create()
        if (deleteAccountAlertDialog.getWindow() != null) {
            deleteAccountAlertDialog.getWindow()!!
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            deleteAccountAlertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        deleteAccountDialogMessage.text =
            StringBuilder().append("Are you sure you want to delete your account?  This action can't be undone!")

        deleteAccountNoButtonDialog.setOnClickListener {
            deleteAccountAlertDialog.cancel()
        }

        deletedAccountYesButtonDialog.setOnClickListener {
            val sharedPref = MyGroceryApplication.preferences
            sharedPref.edit().apply {
                clear()
                apply()
            }
            deleteAccountAlertDialog.cancel()
            userViewmodel.deleteUserAccount()
            popBackStackAndGoToLoginPage()

        }


        view.textView19.setOnClickListener {
            alertDialog.show()
        }
        view.textView16.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_ordersViewPagerFragment)
        }
        view.textView18.setOnClickListener {
            findNavController().navigate(R.id.action_userAccountFragment_to_wishListFragment)
        }

        view.deleteAccount_Button.setOnClickListener {
            deleteAccountAlertDialog.show()

        }
        return view
    }

    fun popBackStackAndGoToLoginPage() {
        userViewmodel.clearRecentSearches()
        while (findNavController().backQueue.size>1){
                findNavController().popBackStack(R.id.homePageFragment,true)
        }

        findNavController().navigate(R.id.loginScreenFragment )
        //findNavController().popBackStack(R.id.homePageFragment, true)
    }
}