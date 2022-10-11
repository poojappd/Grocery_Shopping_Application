package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.viewmodels.OrderDetailsViewModel
import com.example.groceryshoppingapplication.viewmodels.OrderDetailsViewModelFactory
import kotlinx.android.synthetic.main.fragment_place_order.*
import kotlinx.android.synthetic.main.fragment_place_order.view.*
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class PlaceOrderFragment : Fragment() {


    val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels {
        OrderDetailsViewModelFactory(requireActivity().applicationContext)
    }
    lateinit var lastView: View
    lateinit var lastCheckView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_place_order, container, false)
        view.apply {
            totalItems_OrderDetail.text = orderDetailsViewModel.totalItems.toString()
            subTotal_OrderDetail.text = orderDetailsViewModel.subTotal.toString()
            val decimal = DecimalFormat("#.00")
            orderTotal_OrderDetail.text = decimal.format(orderDetailsViewModel.subTotal?.plus(15))
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

            credit_card_option.setOnClickListener {activateOption(it)}
            upi_option.setOnClickListener { activateOption(it) }
            cod_option.setOnClickListener { activateOption(it) }
            place_order_button.setOnClickListener {
                orderDetailsViewModel.paymentOption?.let {
                    Toast.makeText(context,"Order placed successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_placeOrderFragment_to_homePageFragment)
                } ?: Toast.makeText(context,"Choose a payment method! ", Toast.LENGTH_SHORT).show()


            }

        }

        return view

    }

    private fun activateOption(it: View) {
        var paymentOption: String? = null
        var checkView: View? = null

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

        orderDetailsViewModel.paymentOption = paymentOption
        it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#838ED5"))
        checkView.visibility = View.VISIBLE
        if (this@PlaceOrderFragment::lastView.isInitialized) {
            lastView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C7C9D6"))
            lastCheckView.visibility = View.GONE
        }
        lastView = it
        lastCheckView = checkView
    }
}


//#838ED5