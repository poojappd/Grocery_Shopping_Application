package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.WishListItemData
import com.example.groceryshoppingapplication.adapters.WishListItemsAdapter
import com.example.groceryshoppingapplication.listeners.WishListListener
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_addresses.view.*
import kotlinx.android.synthetic.main.fragment_wish_list.view.*

class WishListFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_wish_list, container, false)
        view.wishListToolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        val recyclerView = view.wishlist_recyclerView
        userViewModel.allWishListItems.observe(viewLifecycleOwner){
            recyclerView.adapter = WishListItemsAdapter(it, WishListTouchListenerImpl())
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
        return view
    }

     inner class WishListTouchListenerImpl: WishListListener{
        override fun removeFromWishList(wishListItemId:Int){
            userViewModel.removeProductFromWishList(wishListItemId)
        }

        override fun getWishListItemExtras(productCode: Int): WishListItemData {
            val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
            return WishListItemData(product.brandName+" "+product.itemName, product.unitPrice, product.capacity,product.capacityUnit.value)
        }

         override fun getContext(): Context {
             return requireContext()
         }

    }

}