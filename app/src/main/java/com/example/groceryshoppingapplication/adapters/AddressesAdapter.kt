package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.User
import kotlinx.android.synthetic.main.address_recyclerview_layout.view.*

class AddressesAdapter(private val addresses: List<Address>, private val user: User) :
    RecyclerView.Adapter<AddressesAdapter.AddressesViewHolder>() {

    inner class AddressesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val defaultCheckBox = view.materialRadioButton
        val nickname = view.address_nickname
        val userFullName = view.materialTextView7
        val address = view.materialTextView8
        val contact = view.materialTextView10
        val defaultMarker = view.default_address

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_recyclerview_layout, parent, false)
        return AddressesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        val currentAddress = addresses[position]
        holder.apply {
            if (position == 0){
                holder.defaultMarker.visibility = View.VISIBLE
            }
            nickname.text = currentAddress.addressTag
            userFullName.text =
                StringBuilder().append("${user.firstName} ${user.lastName}").toString()
            address.text = StringBuilder().append(
                "${currentAddress.houseNo}, ${currentAddress.streetDetails}, ${currentAddress.areaDetails}, ${currentAddress.city} - ${currentAddress.pincode}"
            ).toString()
            contact.text = user.mobileNumber
        }
    }

    override fun getItemCount() = addresses.size
}