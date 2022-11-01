package com.example.groceryshoppingapplication.fragments

import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.AddressesAdapter
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_addresses.view.*

class AddressesFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    private val args: AddressesFragmentArgs by navArgs()

    override fun onResume() {
        requireActivity().bottomNavigationView.visibility = View.GONE
        super.onResume()
        Log.e(ContentValues.TAG, "ONResumE CART FRAGMENT")

    }

    override fun onStop() {
        requireActivity().bottomNavigationView.visibility = View.VISIBLE
        super.onStop()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_addresses, container, false)
        val recyclerView = view.address_recyclerView
        val preventDeleteToast = Toast.makeText(
            requireContext(),
            "Default address cannot be deleted",
            Toast.LENGTH_SHORT
        )
        val emptyAddressLayout = view.empty_address_layout

        view.add_address.setOnClickListener {
            if (args.navigateToDeliverySlot) {
                val action =
                    AddressesFragmentDirections.actionAddressesFragmentToEditAddressFragment(
                        navigateToDeliverySlotFragment = true
                    )
                Log.e(TAG, action.toString())
                findNavController().navigate(action)

            } else {

                findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment)
            }
        }
        view.addresses_toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        userViewModel.currentUserAddresses.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyAddressLayout.isVisible = true
                val path = Path().apply {
                    arcTo(300f, 690f, 600f, 990f, 0f, 359f, true)
                }
                    ObjectAnimator.ofFloat(view.search_icon_addresses, View.X, View.Y, path).apply {
                        duration = 2000
                        start()
                    }
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyAddressLayout.visibility = View.GONE
                val user = userViewModel.currentUser.value!!
                val defaultAddress = userViewModel.currentUserDefaultAddress.value!!
                Log.e(TAG,userViewModel.currentUserChosenAddressPosition.value.toString()+"above observe Position IN ADDRESSES  ")

                userViewModel.currentUserChosenAddressPosition.observe(viewLifecycleOwner) { chosenPosition ->
                    Log.e(TAG,chosenPosition.toString()+"IN ADDRESSES  ")
                    recyclerView.adapter = AddressesAdapter(
                        it,
                        user,
                        defaultAddress,
                        chosenPosition,
                        { position: Int, toDelete: Boolean ->
                            val addressToEdit = it[position]
                            if (!toDelete) {
                                val navigationAction =
                                    AddressesFragmentDirections.actionAddressesFragmentToEditAddressFragment(
                                        addressToEdit.addressId
                                    )
                                findNavController().navigate(navigationAction)
                            } else {
                                userViewModel.deleteUserAddress(addressToEdit)
                            }
                        },
                        { preventDeleteToast.show() },
                        { chosenAddressId: String ->
                            userViewModel.updateChosenAddressPosition(chosenAddressId)
                            findNavController().navigate(R.id.action_addressesFragment_to_deliverySlotFragment)
                        })
                    recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
                }
            }

        }
        return view
    }

}