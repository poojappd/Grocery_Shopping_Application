package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.User
import kotlinx.android.synthetic.main.address_recyclerview_layout.view.*

class AddressesAdapter(private val addresses: List<Address>, private val user: User, private val defaultAddress: Address, private val chosenAddressPosition:Int, private val deleteTouchListener:(Int, Boolean)->Unit, private val preventDefaultDeleteListener:()->Unit, private val updateChosenAddressListener:(String)->Unit) :
    RecyclerView.Adapter<AddressesAdapter.AddressesViewHolder>() {
    private var lastChosenRadioButton:RadioButton? = null
    inner class AddressesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val chosenCheckBox = view.materialRadioButton
        val nickname = view.address_nickname
        val userFullName = view.materialTextView7
        val address = view.materialTextView8
        val contact = view.materialTextView10
        val defaultMarker = view.default_address
        val deleteAddress = view.deleteAddressButton
        val editAddress = view.editAddressButton

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_recyclerview_layout, parent, false)
        return AddressesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        val currentAddress = addresses[position]
        holder.apply {
            if (currentAddress.addressId == defaultAddress.addressId){
                holder.defaultMarker.visibility = View.VISIBLE
            }
            if(chosenAddressPosition == position){
                activateOption(holder.chosenCheckBox)
            }
            nickname.text = currentAddress.addressTag?: ""
            val lastNameText =" "+( user.lastName?:"")
            userFullName.text =
                StringBuilder().append("${user.firstName}$lastNameText").toString()
            address.text = StringBuilder().append(
                "${currentAddress.houseNo}, ${currentAddress.streetDetails}, ${currentAddress.areaDetails}, ${currentAddress.city} - ${currentAddress.pincode}"
            ).toString()
            contact.text = user.mobileNumber
            chosenCheckBox.setOnClickListener {
                if (position!=chosenAddressPosition) {
                    activateOption(it as RadioButton)
                    updateChosenAddressListener(currentAddress.addressId)
                }
            }
            deleteAddress.setOnClickListener {
                if(currentAddress.addressId != defaultAddress.addressId)
                    deleteTouchListener(position, true)
                else
                    preventDefaultDeleteListener()
            }
            editAddress.setOnClickListener {
                deleteTouchListener(position,false)
            }
        }
    }

    override fun getItemCount() = addresses.size

    private fun activateOption(radioButton: RadioButton){
        lastChosenRadioButton?.let {
            it.isChecked = false
        }
        radioButton.isChecked = true
        lastChosenRadioButton = radioButton

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}