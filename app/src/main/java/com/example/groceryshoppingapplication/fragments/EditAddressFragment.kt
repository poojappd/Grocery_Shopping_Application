package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.Utils.ValidationService
import com.example.groceryshoppingapplication.enums.AddressTag
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_address.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*


class EditAddressFragment : Fragment() {
    private var lastViewClicked: View? = null
    private var isOtherTagSelected = false
    private var lastClickedaddressTagButtonConfiguration: AddressTagButtonConfiguration? = null


    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val args: EditAddressFragmentArgs by navArgs()

    override fun onResume() {
        requireActivity().bottomNavigationView.visibility = View.GONE
        super.onResume()
    }

    override fun onStop() {
        requireActivity().bottomNavigationView.visibility = View.VISIBLE
        super.onStop()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.example.groceryshoppingapplication.R.layout.fragment_edit_address,
            container,
            false
        )
        val cities =
            requireContext().resources.getStringArray(com.example.groceryshoppingapplication.R.array.cities)
        val adapter = ArrayAdapter(
            this.requireContext(),
            com.example.groceryshoppingapplication.R.layout.cities_array_adapter,
            cities
        )
        view.toolbar_addressEdit.title =
            if (args.addressIdToDisplay != null) "Edit Address" else "Add Address"
        view.city_autocomplete.setAdapter(adapter)
        val toolbar = view.toolbar_addressEdit
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        view.materialTextView14.setOnClickListener {
            activateTag2(
                AddressTagButtonConfiguration(it, AddressTag.HOME)
            )
        }
        view.materialTextView15.setOnClickListener {
            activateTag2(
                AddressTagButtonConfiguration(it, AddressTag.WORK)
            )
        }
        view.materialTextView16.setOnClickListener {

            activateTag2(
                AddressTagButtonConfiguration(
                    it,
                    AddressTag.OTHER,
                    view.other_address_tag,
                    view.other_address_tag_et
                )
            )
        }
        view.pincode_et.doOnTextChanged { text, start, before, count ->
            if (view.pincode.isErrorEnabled()) {
                view.pincode.setErrorEnabled(false)
            }
        }
        view.house_no_edittext.doOnTextChanged { text, start, before, count ->
            if (view.house_no_layout.isErrorEnabled()) {
                view.house_no_layout.setErrorEnabled(false)
            }
        }
        view.area_detail_et.doOnTextChanged { text, start, before, count ->
            if (view.area_detail.isErrorEnabled()) {
                view.area_detail.setErrorEnabled(false)
            }
        }
        view.landmark_et.doOnTextChanged { text, start, before, count ->
            if (view.landmark.isErrorEnabled()) {
                view.landmark.setErrorEnabled(false)
            }
        }
        view.street_detail_et.doOnTextChanged { text, start, before, count ->
            if (view.street_detail.isErrorEnabled()) {
                view.street_detail.setErrorEnabled(false)
            }
        }
        view.city_autocomplete.doOnTextChanged { text, start, before, count ->
            if (view.city_txlayout.isErrorEnabled()) {
                view.city_txlayout.setErrorEnabled(false)
            }
        }

        view.saveButton_address.setOnClickListener {
            val toSaveAsNew = args.addressIdToDisplay == null
            var validationPassed = true
            val houseNo = view.house_no_edittext
            val streetDetail = view.street_detail_et
            val areaDetail = view.area_detail_et
            val landmark = view.landmark_et
            val city = view.city_autocomplete
            val pincode = view.pincode
            val userId = userViewModel.currentUser.value!!.userId
            val newAddId = CodeGeneratorUtil.generateAddressId(userId)
            val addressId =
                if (toSaveAsNew) newAddId else args.addressIdToDisplay!!
            Log.e(TAG, "ASS ID $addressId ${args.addressIdToDisplay} $userId $newAddId")
            if (lastViewClicked?.id == view.materialTextView16.id) isOtherTagSelected = true
            var newAddressTag: String? = null
            lastClickedaddressTagButtonConfiguration?.let {
                if (it.otherAddressView != null) {
                    val otherAddressText = it.otherAddressEditText?.text
                    newAddressTag =
                        if (TextUtils.isEmpty(otherAddressText)) "Other" else otherAddressText.toString()
                } else {
                    newAddressTag = it.addressTag.value
                }
            }

            //pincode
            if (TextUtils.isEmpty(pincode_et.text)) {
                pincode.setError(Response.FIELD_REQUIRED.message)
                pincode.requestFocus()
                validationPassed = false
            } else {
                if (pincode_et.text?.length != 6) {
                    pincode.setError(Response.PIN_CODE_LENGTH_SHORT.message)
                    pincode_et.requestFocus()
                    validationPassed = false
                }
            }

            //city
            if (!cities.contains(city.text.toString())) {
                view.city_txlayout.error = Response.CITY_NOT_VALID.message
                view.city_txlayout.requestFocus()
                validationPassed = false
            }
            //landmark
            if (!TextUtils.isEmpty(landmark.text ) && landmark.text.toString().trim()!="") {
                if (!ValidationService.validateArea(landmark.text.toString().trim())) {
                    view.landmark.setError(Response.LANDMARK_INVALID.message)
                    view.landmark.requestFocus()
                    validationPassed = false
                }
            }
            //area
            if (TextUtils.isEmpty(areaDetail.text)) {
                view.area_detail.setError(Response.FIELD_REQUIRED.message)
                view.area_detail.requestFocus()
                validationPassed = false
            } else {
                if (!ValidationService.validateArea(areaDetail.text.toString().trim())) {
                    view.area_detail.setError(Response.AREA_INVALID.message)
                    view.area_detail.requestFocus()
                    validationPassed = false
                }
            }
            //street
            if (TextUtils.isEmpty(streetDetail.text)) {
                view.street_detail.setError(Response.FIELD_REQUIRED.message)
                view.street_detail.requestFocus()
                validationPassed = false
            } else {
                if (!ValidationService.validateArea(streetDetail.text.toString().trim())) {
                    validationPassed = false
                    view.street_detail.setError(Response.STREET_INVALID.message)
                    view.street_detail.requestFocus()
                }
            }
            //house no
            if (TextUtils.isEmpty(houseNo.text)) {
                view.house_no_layout.setError(Response.FIELD_REQUIRED.message)
                house_no_layout.requestFocus()
                validationPassed = false
            } else {
                if (!ValidationService.validateHouseNumber(houseNo.text.toString().trim())) {
                    validationPassed = false
                    view.house_no_layout.error = Response.HOUSE_NO_INVALID.message
                    view.house_no_layout.requestFocus()
                }
            }


            if (validationPassed) {

                val address = Address(
                    userId,
                    pincode_et.text.toString(),
                    houseNo.text.toString(),
                    streetDetail.text.toString(),
                    landmark.text.toString(),
                    areaDetail.text.toString(),
                    city.text.toString(),
                    newAddressTag,
                    addressId
                )
                if (toSaveAsNew) {
                    userViewModel.addUserAddress(address)
                    val sharedPref = MyGroceryApplication.preferences
                    if (sharedPref.getString("loggedUserMobile", null) == null) {
                        sharedPref.edit().apply {
                            putString("loggedUserDefaultAddressId", addressId)
                            apply()
                        }
                    }
                } else userViewModel.updateUserAddress(address)
                if (args.navigateToDeliverySlotFragment) {
                    findNavController().navigate(com.example.groceryshoppingapplication.R.id.action_editAddressFragment_to_deliverySlotFragment)

                } else {
                    findNavController().popBackStack()
                }
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val houseNo = view.house_no_edittext
        val streetDetail = view.street_detail_et
        val areaDetail = view.area_detail_et
        val city = view.city_autocomplete

        if (args.addressIdToDisplay != null) {
            var userAddress: Address? = null
            userViewModel.currentUserAddresses.value?.forEach {
                if (it.addressId == args.addressIdToDisplay)
                    userAddress = it
            }
            userAddress?.let {
                houseNo.setText(it.houseNo)
                streetDetail.setText(it.streetDetails)
                areaDetail.setText(it.areaDetails)
                if (it.landmark != "null") view.landmark_et.setText(it.landmark)
                city.setText(it.city)
                pincode_et.setText(it.pincode)
                it.addressTag?.let {
                    when (it) {
                        "Home" -> {
                            activateTag2(
                                AddressTagButtonConfiguration(
                                    view.materialTextView14,
                                    AddressTag.HOME
                                )
                            )
                        }
                        "Work" -> {
                            activateTag2(
                                AddressTagButtonConfiguration(
                                    view.materialTextView15,
                                    AddressTag.WORK
                                )
                            )
                        }
                        else -> {
                            if (it != "Other")
                                view.other_address_tag_et.setText(it)

                            activateTag2(
                                AddressTagButtonConfiguration(
                                    view.materialTextView16,
                                    AddressTag.OTHER,
                                    view.other_address_tag,
                                    view.other_address_tag_et
                                )
                            )
                        }
                    }
                }
            }


        }
        super.onViewCreated(view, savedInstanceState)
    }


    private class AddressTagButtonConfiguration(
        val button: View,
        val addressTag: AddressTag,
        val otherAddressView: View? = null,
        val otherAddressEditText: EditText? = null
    )


    private fun activateTag2(addressTagButtonConfiguration: AddressTagButtonConfiguration) {
        if (lastClickedaddressTagButtonConfiguration == null || addressTagButtonConfiguration.addressTag != lastClickedaddressTagButtonConfiguration?.addressTag) {
            addressTagButtonConfiguration.button.background.setTint(Color.parseColor("#AFB9FB"))
            addressTagButtonConfiguration.otherAddressView?.visibility = View.VISIBLE
            lastClickedaddressTagButtonConfiguration?.let {
                it.button.background?.setTint(Color.parseColor("#E0E0E1"))
                if (it.otherAddressView != null) {
                    it.otherAddressView.visibility = View.GONE
                }
            }
            lastClickedaddressTagButtonConfiguration = addressTagButtonConfiguration
        }
    }


}