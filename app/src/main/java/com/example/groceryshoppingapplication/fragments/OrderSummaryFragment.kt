package com.example.groceryshoppingapplication.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateUtils
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import kotlinx.android.synthetic.main.fragment_place_order.view.*
import kotlinx.android.synthetic.main.fragment_place_order.view.materialTextView19
import kotlinx.android.synthetic.main.fragment_place_order.view.materialTextView21
import kotlinx.android.synthetic.main.fragment_place_order.view.materialTextView22
import kotlinx.android.synthetic.main.fragment_place_order.view.orderTotal_OrderDetail
import kotlinx.android.synthetic.main.fragment_place_order.view.subTotal_OrderDetail
import kotlinx.android.synthetic.main.fragment_place_order.view.totalItems_OrderDetail
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class OrderSummaryFragment : Fragment() {

    val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModelFactory(requireActivity().applicationContext)
    }
    private val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val args: OrderSummaryFragmentArgs by navArgs()
    private val modifyOrderViewModel: ModifyOrderViewModel by activityViewModels {
        ModifyOrderViewModelFactory(requireContext().applicationContext)
    }

    override fun onResume() {
        requireActivity().bottomNavigationView.visibility = View.GONE
        super.onResume()

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
        val view = inflater.inflate(
            R.layout.fragment_order_summary,
            container,
            false
        )
        //setHasOptionsMenu(true)
        view.apply {

            orderHistoryViewModel.getOrderDetail(args.orderId).observe(viewLifecycleOwner) {
                val orderDetail = it
                orderSummary_collapsingToolbar.title = "Order " + it.orderStatus.value
                orderId_collapsingToolbar.text = orderDetail.orderId
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



                toolbar_OrderSummary.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
                val decimal = DecimalFormat("#.00")

                val toolbar = toolbar_OrderSummary
                if (orderDetail.orderStatus == OrderStatus.ORDERED) {
                    toolbar.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.cancel_order_menu -> {
                                // Navigate to settings screen
                                alertDialog.show()
                                Toast.makeText(requireContext(), "Yes", Toast.LENGTH_SHORT).show()
                                true
                            }
                            R.id.removeItems_menu -> {
                                if (modifyOrderViewModel.modifiedSessionEnabled) {
                                    modifyOrderViewModel.haltModifyingOrder()
                                }
                                modifyOrderViewModel.setOrderDetails(
                                    orderHistoryViewModel.getOrderItemsFromOrder(orderDetail.orderId),
                                    orderHistoryViewModel.getOrderDetail(orderDetail.orderId).value!!,
                                )


                                findNavController().navigate(R.id.action_orderSummaryFragment_to_modifyOrderFragment)
                                true
                            }
                            else -> false
                        }
                    }

                } else {
                    toolbar.menu.findItem(R.id.overFlowMenu_orderSummary).isVisible = false
                }
                paymentMethod.text = orderDetail.paymentMethod.capitalize()

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


                val actualFormat = SimpleDateFormat("dd MMM yyyy - h a", Locale.getDefault())
                val dayFormat = SimpleDateFormat("dd MMM yyyy")
                val slotDateValue =
                    dayFormat.parse(dayFormat.format(actualFormat.parse(orderDetail.deliverySlot)))
                val today = dayFormat.parse(dayFormat.format(Date().time))
                val diff: Long = slotDateValue.getTime() - today.getTime()
                println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))

                when (orderDetail.orderStatus) {
                    OrderStatus.COMPLETE -> arrivingOnTV.text = StringBuilder("Delivered on")
                    OrderStatus.CANCELLED -> arrivingOnTV.text = StringBuilder("Delivery slot")
                    else -> {
                        when (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) {
                            0L -> {
                                val time = SimpleDateFormat("ha").format(actualFormat.parse(orderDetail.deliverySlot))
                                arrivingTime.text = StringBuilder("Today $time")
                            }
                            1L -> {
                                val time = SimpleDateFormat("ha").format(actualFormat.parse(orderDetail.deliverySlot))
                                arrivingTime.text = StringBuilder("Tomorrow $time")
                            }
                            else -> { arrivingTime.text = StringBuilder(orderDetail.deliverySlot)}
                        }
                    }

                }





            order_date.text = StringBuilder(orderDetail.orderDate)
            when (orderDetail.orderStatus) {
                OrderStatus.ORDERED -> {
                    order_status_icon.setImageResource(R.drawable.time_icon1)
                }
                OrderStatus.COMPLETE -> {
                    order_status_icon.setImageResource(R.drawable.complete_icon)
                }
                OrderStatus.CONFIRMED -> {
                    order_status_icon.setImageResource(R.drawable.check_icon1)
                }
                OrderStatus.CANCELLED -> {
                    order_status_icon.setImageResource(R.drawable.cancel_icon)
                }
            }
            order_status.text = orderDetail.orderStatus.value

        }
    }

    return view
}
}