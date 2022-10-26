package com.example.groceryshoppingapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import kotlinx.android.synthetic.main.fragment_place_order.view.*
import kotlinx.android.synthetic.main.fragment_place_order.view.materialTextView19
import kotlinx.android.synthetic.main.fragment_place_order.view.materialTextView21
import kotlinx.android.synthetic.main.fragment_place_order.view.materialTextView22
import kotlinx.android.synthetic.main.fragment_place_order.view.orderTotal_OrderDetail
import kotlinx.android.synthetic.main.fragment_place_order.view.subTotal_OrderDetail
import kotlinx.android.synthetic.main.fragment_place_order.view.toolbar_placeOrder
import kotlinx.android.synthetic.main.fragment_place_order.view.totalItems_OrderDetail
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderSummaryFragment : Fragment() {

    val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModelFactory(requireActivity().applicationContext)
    }
    private val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val args: OrderSummaryFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_summary, container, false)
        view.apply {
            toolbar_placeOrder.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
            val decimal = DecimalFormat("#.00")

            val orderDetail = orderHistoryViewModel.getOrderDetail(args.orderId)
            totalItems_OrderDetail.text = orderDetail.numberOfItems.toString()
            subTotal_OrderDetail.text = decimal.format(orderDetail.subTotal)
            orderTotal_OrderDetail.text = decimal.format(orderDetail.totalPrice)
            orderDetail.deliveryAddress.apply {
                materialTextView19.text = StringBuilder().append(
                    "${this.houseNo}, ${this.streetDetails}, ${this.areaDetails}," +
                            "${this.city} - ${this.pincode}"
                )
            }

            view_orderItems_button.setOnClickListener {
                val action = OrderSummaryFragmentDirections.actionOrderSummaryFragmentToOrderedItemsListFragment(args.orderId)
                findNavController().navigate(action)
            }

            val dateFormat = SimpleDateFormat("EEEE dd MMMM ", Locale.getDefault())
            materialTextView21.text =
                orderDetail.deliverySlot//StringBuilder().append(dateFormat.format(orderDetailsViewModel.deliverySlot!!)+"-")
            val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
            materialTextView22.text =
                orderDetail.deliverySlot//timeFormat.format(orderDetailsViewModel.deliverySlot!!)
            order_date.text = orderDetail.orderDate
            order_status.text = orderDetail.orderStatus.value


        }
        return view
    }
}