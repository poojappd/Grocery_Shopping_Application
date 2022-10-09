package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.AddressesAdapter
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_addresses.*
import kotlinx.android.synthetic.main.fragment_addresses.view.*

class AddressesFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_addresses, container, false)
        val recyclerView = view.address_recyclerView
        view.add_address.setOnClickListener {
            findNavController().navigate(R.id.action_addressesFragment_to_editAddressFragment)
        }
        userViewModel.currentUserAddresses.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
                userViewModel.currentUser.observe(viewLifecycleOwner) { user->
                    recyclerView.adapter = AddressesAdapter(it,user)
                    recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
                }
            }
        }
        return view
    }

}