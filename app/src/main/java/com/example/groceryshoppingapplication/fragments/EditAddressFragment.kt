package com.example.groceryshoppingapplication.fragments
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.SharedPrefViewModel
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.fragment_edit_address.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*

class EditAddressFragment : Fragment() {
    var lastViewClicked: View? = null

    private val sharedPrefViewModel: SharedPrefViewModel by lazy {
        SharedPrefViewModel(requireActivity().application)
    }
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    private val args : EditAddressFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_address, container, false)
        val cities = requireContext().resources.getStringArray(R.array.cities)
        val adapter = ArrayAdapter(this.requireContext(),R.layout.cities_array_adapter, cities)
        view.city_autocomplete.setAdapter(adapter)
        var addressTag:String? = null
        var isOtherTagSelected = false
        val otherTagText = view.other_address_tag

        view.materialTextView14.setOnClickListener {
            addressTag = "Home"
            disableOtherTags(it, otherTagText)
        }
        view.materialTextView15.setOnClickListener {
            addressTag = "Work"
            disableOtherTags(it,otherTagText)
        }
        view.materialTextView16.setOnClickListener {
                isOtherTagSelected=true
                disableOtherTags(it)
                otherTagText.visibility = View.VISIBLE
        }

        view.saveButton_address.setOnClickListener {

            val houseNo = view.textInputEditText.text.toString()
            val streetDetail = view.street_detail.text.toString()
            val areaDetail = view.area_detail.text.toString()
            val landmark = if(TextUtils.isEmpty(view.landmark.text)) null else view.landmark.text.toString()
            val city = view.city_autocomplete.text.toString()
            val pincode = view.pincode.text.toString()
            val userId = userViewModel.getCurrentUserData(sharedPrefViewModel.userMobile!!)!!.userId
            val addressId = CodeGeneratorUtil.generateAddressId(userId)
            if (lastViewClicked?.id == view.materialTextView16.id) isOtherTagSelected = true
            val newAddressTag = if(isOtherTagSelected) otherTagText.text.toString() else addressTag

            val address = Address(userId, pincode, houseNo, streetDetail, landmark, areaDetail, city,newAddressTag,addressId)

            userViewModel.addUserAddress(address)
            if(args.navigateToDeliverySlotFragment == true){
               findNavController().navigate(R.id.action_editAddressFragment_to_deliverySlotFragment)

            }
            else{
            findNavController().popBackStack()}
        }
        return view
    }

private fun disableOtherTags(it:View, otherTagText:View? = null){
    it.background.setTint(Color.parseColor("#AFB9FB"))
    lastViewClicked?.background?.setTint(Color.parseColor("#E0E0E1"))
    if(otherTagText?.visibility == View.VISIBLE) otherTagText.visibility = View.GONE
    lastViewClicked = it
}

}