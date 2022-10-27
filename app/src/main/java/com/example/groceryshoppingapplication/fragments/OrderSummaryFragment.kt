package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.OrderStatus
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
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*
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
        val view = inflater.inflate(
            com.example.groceryshoppingapplication.R.layout.fragment_order_summary,
            container,
            false
        )
        //setHasOptionsMenu(true)
        view.apply {

            orderHistoryViewModel.getOrderDetail(args.orderId).observe(viewLifecycleOwner) {
                val orderDetail = it

                val dialogBuilder = AlertDialog.Builder(requireContext())
                val dialogView =
                    layoutInflater.inflate(R.layout.mobilenumber_chage_alert_layout, null)
                dialogBuilder.setView(dialogView)
                val dialogMessage = dialogView.dialog_message
                val yesButtonDialog = dialogView.button
                val noButtonDialog = dialogView.no
                val alertDialog = dialogBuilder.create()
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow()!!
                        .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                    alertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
                }
                dialogMessage.text =
                    StringBuilder().append("Are you sure you want to cancel your order?")
                yesButtonDialog.setOnClickListener { view ->
                    orderHistoryViewModel.updateOrderStatus(
                        OrderStatus.CANCELLED,
                        orderDetail.orderId
                    )
                    Toast.makeText(
                        requireContext(),
                        "Your order has been cancelled!",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                    alertDialog.cancel()
                }

                noButtonDialog.setOnClickListener {
                    alertDialog.cancel()
                }



                toolbar_placeOrder.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
                val decimal = DecimalFormat("#.00")

                val toolbar = toolbar_placeOrder
                if (orderDetail.orderStatus == OrderStatus.ORDERED) {
                    toolbar.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.cancel_order_menu -> {
                                // Navigate to settings screen
                                alertDialog.show()
                                Toast.makeText(requireContext(), "Yes", Toast.LENGTH_SHORT).show()
                                true
                            }
                            else -> false
                        }
                    }
                } else {
                    toolbar.menu.findItem(R.id.cancel_order_menu).isVisible = false
                }

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
                    val action =
                        OrderSummaryFragmentDirections.actionOrderSummaryFragmentToOrderedItemsListFragment(
                            args.orderId
                        )
                    findNavController().navigate(action)
                }

                val dateFormat = SimpleDateFormat("EEEE dd MMMM ", Locale.getDefault())
                materialTextView21.text =
                    orderDetail.deliverySlot
                val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
                materialTextView22.text =
                    orderDetail.deliverySlot
                val deliveryDate =
                    SimpleDateFormat("dd MMM yyyy - hh a").parse(orderDetail.deliverySlot)

                order_date.text = orderDetail.orderDate
                if (Date().after(deliveryDate)){
                    if(it.orderStatus!=OrderStatus.CANCELLED) {
                        order_status.text = OrderStatus.COMPLETE.value
                    }
                    else{
                        order_status.text = it.orderStatus.value
                    }
                    view.DeliverOnTextView.text = "Delivered on"
                }
                else{
                    order_status.text = it.orderStatus.value

                }
            }
        }
        return view
    }
}