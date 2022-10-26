package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Index
import com.example.groceryshoppingapplication.CartItemData
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.OrderedItemsAdapter
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.OrderHistoryViewModel
import com.example.groceryshoppingapplication.viewmodels.OrderHistoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_ordered_items_list.view.*
import kotlinx.android.synthetic.main.fragment_place_order.view.*

class OrderedItemsListFragment : Fragment() {


    private val args: OrderedItemsListFragmentArgs by navArgs()
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModelFactory(requireActivity().applicationContext)
    }
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ordered_items_list, container, false)
        view.toolbar_orderedItems.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        val recyclerView = view.orderedItems_recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val orderedItems = orderHistoryViewModel.getOrderItemsFromOrder(args.orderId)
        recyclerView.adapter = OrderedItemsAdapter(orderedItems){productCode:Int->
            val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
            CartItemData( product.brandName+ " "+ product.itemName, product.unitPrice )
        }
        return view

    }


}