package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
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
        val view = inflater.inflate(com.example.groceryshoppingapplication.R.layout.fragment_edit_address, container, false)
        val cities = requireContext().resources.getStringArray(com.example.groceryshoppingapplication.R.array.cities)
        val adapter = ArrayAdapter(this.requireContext(), com.example.groceryshoppingapplication.R.layout.cities_array_adapter, cities)
        view.toolbar_addressEdit.title = if(args.addressIdToDisplay != -1 ) "Edit Address" else "Add Address"
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
                AddressTagButtonConfiguration(it, AddressTag.OTHER, view.other_address_tag)
            )
        }

        view.saveButton_address.setOnClickListener {
//            val sharedPref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
//            val userId = sharedPref.getString("loggedUserId","")
            val toSaveAsNew = args.addressIdToDisplay == -1
            var validationPassed = true
            val houseNo = view.textInputEditText
            val streetDetail = view.street_detail
            val areaDetail = view.area_detail
            val landmark = if (TextUtils.isEmpty(view.landmark.text)) null else view.landmark.text
            val city = view.city_autocomplete
            val pincode = view.pincode
            val userId = userViewModel.currentUser.value!!.userId

            val addressId = if(toSaveAsNew) CodeGeneratorUtil.generateAddressId(userId) else args.addressIdToDisplay
            if (lastViewClicked?.id == view.materialTextView16.id) isOtherTagSelected = true
            var newAddressTag: String? = null
            lastClickedaddressTagButtonConfiguration?.let {
                if (it.otherAddressView != null) {
                    val otherAddressText = it.otherAddressView.text
                    newAddressTag =
                        if (TextUtils.isEmpty(otherAddressText)) "Other" else otherAddressText.toString()
                } else {
                    newAddressTag = it.addressTag.value
                }
            }

            if (TextUtils.isEmpty(houseNo.text)) {
                houseNo.setError(Response.FIELD_REQUIRED.message)
                validationPassed = false
            }
            if (TextUtils.isEmpty(streetDetail.text)) {
                streetDetail.setError(Response.FIELD_REQUIRED.message)
                validationPassed = false
            }
            if(TextUtils.isEmpty(areaDetail.text)){
                areaDetail.setError(Response.FIELD_REQUIRED.message)
                validationPassed = false
            }
            if (TextUtils.isEmpty(pincode.text)) {
                pincode.setError(Response.FIELD_REQUIRED.message)
                validationPassed = false
            } else {
                if (pincode.text?.length != 6) {
                    pincode.setError(Response.PIN_CODE_LENGTH_SHORT.message)
                    validationPassed = false
                }
            }
            if (!cities.contains(city.text.toString())) {
                city.setError("Choose a city from the drop down")
                validationPassed = false
            }

            if (validationPassed) {
                val address = Address(
                    userId,
                    pincode.text.toString(),
                    houseNo.text.toString(),
                    streetDetail.text.toString(),
                    landmark.toString(),
                    areaDetail.text.toString(),
                    city.text.toString(),
                    newAddressTag,
                    addressId
                )
                if(toSaveAsNew){
                userViewModel.addUserAddress(address)
                }
                else userViewModel.updateUserAddress(address)
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
        val houseNo = view.textInputEditText
        val streetDetail = view.street_detail
        val areaDetail = view.area_detail
        val city = view.city_autocomplete

        if (args.addressIdToDisplay != -1) {
            var userAddress: Address? = null
            userViewModel.currentUserAddresses.value?.forEach {
                if (it.addressId == args.addressIdToDisplay)
                    userAddress = it
            }
            userAddress?.let {
                houseNo.setText(it.houseNo)
                streetDetail.setText(it.streetDetails)
                areaDetail.setText(it.areaDetails)
                if (it.landmark != "null") view.landmark.setText(it.landmark)
                city.setText(it.city)
                pincode.setText(it.pincode)
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
                            if(it!= "Other")
                                view.other_address_tag.setText(it)

                            activateTag2(
                            AddressTagButtonConfiguration(
                                view.materialTextView16,
                                AddressTag.OTHER,
                                view.other_address_tag
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
        val otherAddressView: TextView? = null
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