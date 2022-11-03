package com.example.groceryshoppingapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.models.OrderDetail
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.orders_recyclerview_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class OrdersAdapter(
    private val orderDetailList: List<OrderDetail>,
    private val navigationListener: (String) -> Unit,
    private val orderStatusUpdationListener: (orderId: String, orderStatus: OrderStatus) -> Unit
) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderedItemEntityLayout = view.orderedItemEntity_layout
        val orderIdTv = view.orderId_ordersRv
        val orderedDateTv = view.orderedDate_tv
        val deliverySlotTv = view.deliverySlot_ordersRv
        val itemCountTv = view.itemCount_OrdersRv
        val totalPriceTv = view.materialTextView26
        val orderStatusTv: MaterialTextView = view.orderStatus_tv_ordersRv

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_recyclerview_layout, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {

        val orderDetailItem = orderDetailList.get(position)
        val simpleDateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val deliverySlotPrefix =
            if (orderDetailItem.orderStatus == OrderStatus.ORDERED) "Delivery on: " else if (orderDetailItem.orderStatus == OrderStatus.COMPLETE) "Delivered on: " else "Delivery slot: "
        val itemCountSuffix = if (orderDetailItem.numberOfItems > 1) " Items" else " Item"

        val date = SimpleDateFormat("dd MMM yyyy - hh:mma").parse(orderDetailItem.orderDate)
        val oneHourAfterOrderedDate = Calendar.getInstance()
        oneHourAfterOrderedDate.time = date
        oneHourAfterOrderedDate.add(Calendar.HOUR, 1)

        val deliveryDate =
            SimpleDateFormat("dd MMM yyyy - hh a").parse(orderDetailItem.deliverySlot)

        holder.apply {
            orderIdTv.text = orderDetailItem.orderId
            orderedDateTv.text = simpleDateFormat.format(Date(date.time))

            deliverySlotTv.text =
                StringBuilder().append(deliverySlotPrefix + orderDetailItem.deliverySlot)
            itemCountTv.text =
                StringBuilder().append(orderDetailItem.numberOfItems.toString() + itemCountSuffix)
            totalPriceTv.text =
                StringBuilder().append("Rs. " + orderDetailItem.totalPrice.toString())

            orderedItemEntityLayout.setOnClickListener {
                navigationListener(orderDetailItem.orderId)
            }
            orderStatusTv.text =
                when (orderDetailItem.orderStatus) {
                    OrderStatus.CANCELLED -> {
                        orderStatusTv.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.cancel_icon,
                            0,
                            0,
                            0
                        )
                        OrderStatus.CANCELLED.value
                    }

                    OrderStatus.CONFIRMED -> {
                        if (Date().after(deliveryDate)) {
                            orderStatusUpdationListener(orderDetailItem.orderId, OrderStatus.COMPLETE)
                            orderStatusTv.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.complete_icon,
                                0,
                                0,
                                0
                            )
                            "Complete"
                        } else {
                            orderStatusTv.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.complete_icon,
                                0,
                                0,
                                0
                            )
                            OrderStatus.CONFIRMED.value
                        }
                    }

                    OrderStatus.ORDERED -> {
                        if (oneHourAfterOrderedDate.after(Date())) {
                            orderStatusUpdationListener(
                                orderDetailItem.orderId,
                                OrderStatus.CONFIRMED
                            )
                            orderStatusTv.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.complete_icon,
                                0,
                                0,
                                0
                            )
                            OrderStatus.CONFIRMED.value
                        } else
                            "Placed"
                    }
                    else -> {
                        orderStatusTv.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.complete_icon,
                            0,
                            0,
                            0
                        )
                        OrderStatus.COMPLETE.value
                    }


                }

        }
    }

    override fun getItemCount() = orderDetailList.size
}
