package com.example.groceryshoppingapplication.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_modified_place_order.*
import kotlinx.android.synthetic.main.fragment_modified_place_order.view.*
import kotlinx.android.synthetic.main.fragment_modified_place_order.yetToPayOrRefund
import kotlinx.android.synthetic.main.fragment_modified_place_order.yetToPay_Refund_value
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ModifiedPlaceOrderFragment : Fragment() {

    private val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels {
        OrderDetailsViewModelFactory(requireActivity().applicationContext)
    }
    private val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    private val modifyOrderViewModel: ModifyOrderViewModel by activityViewModels {
        ModifyOrderViewModelFactory(requireContext().applicationContext)
    }

    lateinit var lastView: View
    lateinit var lastCheckView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modified_place_order, container, false)
        val toastOrderplaced = Toast.makeText(context,"Order placed successfully", Toast.LENGTH_SHORT)
        val toastChoosePayment = Toast.makeText(context,"Choose a payment method! ", Toast.LENGTH_SHORT)
        view.apply {
            val modifiedOrderDetail = modifyOrderViewModel.modifiedOrderDetail
            val previousOrderDetail = modifyOrderViewModel.orderDetail
            toolbar_modifieDplaceOrder.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
            val decimal = DecimalFormat("#.00")
            totalItems_OrderDetail.text = modifyOrderViewModel.modifiableOrderItems.value!!.size.toString()
            if (previousOrderDetail.paymentMethod == "credit card"|| previousOrderDetail.paymentMethod =="UPI"){
                PaidAlready.text = decimal.format(previousOrderDetail.totalPrice)
                if(previousOrderDetail.totalPrice >= modifiedOrderDetail.totalPrice){
                    yetToPayOrRefund.text = "Refund"
                    yetToPay_Refund_value.text = (previousOrderDetail.totalPrice - modifiedOrderDetail.totalPrice).toString()
                }
                else{
                    yetToPay_Refund_value.text = (modifiedOrderDetail.totalPrice - previousOrderDetail.totalPrice).toString()
                }
            }
            else{
                paidLayoutCOntainer.visibility = View.GONE
                yetToPay_Refund_value.text = modifiedOrderDetail.totalPrice.toString()
            }
            orderTotal_OrderDetail.text = decimal.format(modifiedOrderDetail.subTotal.plus(15))
            orderDetailsViewModel.deliveringAddress?.apply {
                materialTextView19.text = StringBuilder().append(
                    "${this.houseNo}, ${this.streetDetails}, ${this.areaDetails}," +
                            "${this.city} - ${this.pincode}"
                )
            }

            val dateFormat = SimpleDateFormat("EEEE dd MMMM ", Locale.getDefault())
            materialTextView21.text = StringBuilder().append(dateFormat.format(orderDetailsViewModel.deliverySlot!!)+"-")
            val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
            materialTextView22.text = timeFormat.format(orderDetailsViewModel.deliverySlot!!)

            if(yetToPayOrRefund.text != "Refund") {
                credit_card_option.setOnClickListener { activateOption(it) }
                upi_option.setOnClickListener { activateOption(it) }
                cod_option.setOnClickListener { activateOption(it) }
            }
            else{
                constraintLayout6.visibility = View.GONE
                orderDetailsViewModel.paymentOption = previousOrderDetail.paymentMethod
            }
            //place order
            place_order_button.setOnClickListener {

                orderDetailsViewModel.paymentOption?.let {
                    val action:Int
                    if(!modifyOrderViewModel.modifiedSessionEnabled) {
                        val orderDate = Date()

                        val orderId = CodeGeneratorUtil.generateOrderId(orderDate)
                        val subTotal = decimal.format(orderDetailsViewModel.subTotal).toDouble()
                        val orderDateString =
                            SimpleDateFormat("dd MMM yyyy - h:ma", Locale.getDefault()).format(
                                orderDate
                            )
                        val numberOfItems = orderDetailsViewModel.totalItems!!
                        val deliverySlot =
                            SimpleDateFormat("dd MMM yyyy - h a", Locale.getDefault()).format(
                                orderDetailsViewModel.deliverySlot!!
                            )
                        val deliveryAddress = orderDetailsViewModel.deliveringAddress!!
                        val mobileNumber = orderDetailsViewModel.mobileNumber!!
                        val totalPrice = subTotal + 15
                        val paymentMethod = orderDetailsViewModel.paymentOption!!
                        orderDetailsViewModel.userId?.let {
                            {
                                val newOrder = OrderDetail(
                                    orderId,
                                    it,
                                    subTotal,
                                    orderDateString,
                                    numberOfItems,
                                    deliverySlot,
                                    deliveryAddress,
                                    mobileNumber,
                                    15.0,
                                    totalPrice,
                                    paymentMethod
                                )
                                val orderedItems = mutableListOf<OrderedItemEntity>()
                                var number = 1
                                userViewmodel.allCartItems.value!!.forEach {
                                    orderedItems.add(
                                        OrderedItemEntity(
                                            orderId,
                                            it.productCode,
                                            it.quantity,
                                            "$orderId/${number++}"
                                        )
                                    )
                                }
                                val viewModel: OrderHistoryViewModel by viewModels {
                                    OrderHistoryViewModelFactory(requireContext().applicationContext)
                                }
                                viewModel.createNewOrder(newOrder, orderedItems)
                                orderDetailsViewModel.clearOrderDetails()
                                userViewmodel.emptyCart()
                                inventoryViewModel.reserveProducts(orderedItems)
                            }

                        }
                        action = R.id.action_placeOrderFragment_to_ordersViewPagerFragment
                    }
                    else{
                        val viewModel: OrderHistoryViewModel by viewModels {
                            OrderHistoryViewModelFactory(requireContext().applicationContext)
                        }
                        val newOrder = OrderDetail(
                            modifiedOrderDetail.orderId,
                            modifiedOrderDetail.userId,
                            modifiedOrderDetail.subTotal,
                            modifiedOrderDetail.orderDate,
                            modifiedOrderDetail.numberOfItems,
                            SimpleDateFormat("dd MMM yyyy - h a", Locale.getDefault()).format(
                                orderDetailsViewModel.deliverySlot!!
                            ),
                            orderDetailsViewModel.deliveringAddress!!,
                            modifiedOrderDetail.mobileNumber,
                            0.0,
                            modifiedOrderDetail.totalPrice,
                            it
                        )
                        modifyOrderViewModel.modifiedOrderDetail = newOrder
                        viewModel.updateOrderChanges(modifyOrderViewModel.modifiableOrderItems.value!!, newOrder)
                        modifyOrderViewModel.haltModifyingOrder()
                        action = R.id.action_modifiedPlaceOrderFragment_to_ordersViewPagerFragment
                    }
                    toastOrderplaced.show()
                    findNavController().navigate(action)
                } ?: toastChoosePayment.show()


            }

        }

        return view

    }

    private fun activateOption(it: View) {
        var paymentOption: String? = null
        var checkView: View? = null
        scrollView_placeOrder.post {
            scrollView_placeOrder.fullScroll(View.FOCUS_DOWN)
        }
        when (it.id) {
            R.id.credit_card_option -> {
                paymentOption = "credit card"
                checkView = it.creditCard_check
            }
            R.id.upi_option -> {
                paymentOption = "UPI"
                checkView = it.upi_check
            }
            else -> {
                paymentOption = "Cash on delivery"
                checkView = it.cod_check
            }
        }

        it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#838ED5"))
        checkView.visibility = View.VISIBLE

        orderDetailsViewModel.paymentOption = paymentOption

        if (this@ModifiedPlaceOrderFragment::lastView.isInitialized && lastView.id!=it.id) {
            lastView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C7C9D6"))
            lastCheckView.visibility = View.GONE
        }
        lastView = it
        lastCheckView = checkView
    }
}