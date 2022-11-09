package com.example.groceryshoppingapplication.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.Utils.NotificationReceiver
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_place_order.*
import kotlinx.android.synthetic.main.fragment_place_order.view.*
import kotlinx.android.synthetic.main.mobilenumber_chage_alert_layout.view.*
import kotlinx.android.synthetic.main.product_not_available_dialog_layout.view.*
import kotlinx.android.synthetic.main.product_not_available_dialog_layout.view.button
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class PlaceOrderFragment : Fragment() {


    private val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels {
        OrderDetailsViewModelFactory(requireActivity().applicationContext)
    }
    private val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }


    lateinit var lastView: View
    lateinit var lastCheckView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_place_order, container, false)
        val toastOrderplaced = Toast.makeText(context,"Order placed successfully", Toast.LENGTH_SHORT)
        val toastChoosePayment = Toast.makeText(context,"Choose a payment method! ", Toast.LENGTH_SHORT)
        view.apply {
            toolbar_placeOrder.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
            val decimal = DecimalFormat("#.00")
            totalItems_OrderDetail.text = orderDetailsViewModel.totalItems.toString()
            subTotal_OrderDetail.text = decimal.format(orderDetailsViewModel.subTotal)
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

            //place order
            place_order_button.setOnClickListener {
                orderDetailsViewModel.paymentOption?.let {
                    val orderDate = Date()
                    val orderId = CodeGeneratorUtil.generateOrderId(orderDate)
                    val subTotal = decimal.format(orderDetailsViewModel.subTotal).toDouble()
                    val orderDateString = SimpleDateFormat("dd MMM yyyy - hh:mma", Locale.getDefault()).format(orderDate)
                    val numberOfItems = orderDetailsViewModel.totalItems!!
                    val deliverySlot = SimpleDateFormat("dd MMM yyyy - h a", Locale.getDefault()).format(orderDetailsViewModel.deliverySlot!!)

                    val deliveryAddress = orderDetailsViewModel.deliveringAddress!!
                    val mobileNumber = orderDetailsViewModel.mobileNumber!!
                    val totalPrice = subTotal+15
                    val paymentMethod = orderDetailsViewModel.paymentOption!!
                    orderDetailsViewModel.userId?.let {
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
                            orderedItems.add(OrderedItemEntity(orderId, it.productCode, it.quantity,"$orderId/${number++}"))
                        }
                        val viewModel: OrderHistoryViewModel by viewModels{
                            OrderHistoryViewModelFactory(requireContext().applicationContext)
                        }
                        viewModel.createNewOrder(newOrder,orderedItems)
                        val deliverySlotDate = SimpleDateFormat("h a", Locale.getDefault()).format(orderDetailsViewModel.deliverySlot!!)
                        val orderTimeString = SimpleDateFormat("h:mma", Locale.getDefault()).format(Date())
                        val intent = Intent(requireActivity(), NotificationReceiver::class.java)
                        intent.putExtra("deliveryTime", deliverySlotDate)
                        val sevendayalarm: Calendar = Calendar.getInstance()

                        sevendayalarm.add(Calendar.SECOND,3)
                        val pendinIntent = PendingIntent.getBroadcast(requireActivity(),0,intent,0)
                        val mgr = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
                        val timeforNotification = 1000.toLong()

                        mgr.set(AlarmManager.RTC_WAKEUP,sevendayalarm.timeInMillis,pendinIntent)
                        orderDetailsViewModel.clearOrderDetails()
                        userViewmodel.emptyCart()
                        inventoryViewModel.reserveProducts(orderedItems)

                    }
                    showOrderPlacedDialog()
                    toastOrderplaced.show()


                    findNavController().navigate(R.id.action_placeOrderFragment_to_ordersViewPagerFragment)
                } ?: toastChoosePayment.show()


            }

        }

        return view

    }

    private fun showOrderPlacedDialog(){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogView =
            layoutInflater.inflate(R.layout.order_placed_dialog, null)

        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow()!!
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
        alertDialog.show()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                alertDialog.dismiss()
                timer.cancel()

            }
        }, 3000)


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

        if (this@PlaceOrderFragment::lastView.isInitialized && lastView.id!=it.id) {
            lastView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#C7C9D6"))
            lastCheckView.visibility = View.GONE
        }
        lastView = it
        lastCheckView = checkView
    }
}


//#838ED5