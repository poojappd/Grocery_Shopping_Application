package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.adapters.AddressesAdapter
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.fragment_addresses.view.*
import java.util.*

class AddressesFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    private val args: AddressesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_addresses, container, false)
        val recyclerView = view.address_recyclerView

        view.add_address.setOnClickListener {
            if(args.navigateToDeliverySlot){
                Log.e(TAG, "args provided from cart")
                val action = AddressesFragmentDirections.actionAddressesFragmentToEditAddressFragment(navigateToDeliverySlotFragment = true)
                Log.e(TAG, action.toString())
                findNavController().navigate(action)

            }
            else {

                findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment)
            }
        }
        userViewModel.currentUserAddresses.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
                userViewModel.currentUser.observe(viewLifecycleOwner) { user->
                    recyclerView.adapter = AddressesAdapter(it,user){position:Int, toDelete:Boolean ->
                        val addressToEdit = it[position]
                        if (!toDelete){
                            val navigationAction =
                                AddressesFragmentDirections.actionAddressesFragmentToEditAddressFragment(
                                    addressToEdit.addressId
                                )
                            findNavController().navigate(navigationAction)
                        }
                        else{
                            userViewModel.deleteUserAddress(addressToEdit)
                        }
                    }
                    recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
                }
            }
        }
        return view
    }

}