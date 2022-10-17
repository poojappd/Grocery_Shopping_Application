package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.OrdersAdapter
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_orders_view_pager.view.*


class OrdersViewPagerFragment() : Fragment() {
    private lateinit var applicationContext: Context
constructor(applicationContext: Context) : this() {
    this.applicationContext = applicationContext
}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(!this::applicationContext.isInitialized)
            applicationContext = requireActivity().applicationContext
        val view = inflater.inflate(R.layout.fragment_orders_view_pager, container, false)
        val userId: String
        if (applicationContext!=null) {
            val userViewModel: UserViewModel by activityViewModels {
                UserViewModelFactory(applicationContext)
            }
            userId = userViewModel.currentUser.value!!.userId
            val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
                OrderHistoryViewModelFactory(applicationContext, userId)
            }
            orderHistoryViewModel.refreshMyOrders()
            orderHistoryViewModel.allOrders.observe(viewLifecycleOwner) {
                Log.e(TAG, "INSIDE ORDERSVIEWPAGER FRAGMENT OBSERVE")

                val ordersRecyclerView = view.myOrders_recyclerView_viewPager
                ordersRecyclerView.adapter = OrdersAdapter(it)
                ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
        return view
    }


}