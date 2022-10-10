package com.example.groceryshoppingapplication.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import kotlinx.android.synthetic.main.custom_datepicker_deliveryslot_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class DeliverySlotDateAdapter(private val datesSlots: List<Date>,
                              private val chooseDateListener: (Date,Int)->Unit,
                              private val chosenPosition:Int? = null
) :
    RecyclerView.Adapter<DeliverySlotDateAdapter.DeliverySlotViewHolder>() {
    private val dateFormat = SimpleDateFormat("dd EEEE", Locale.getDefault())
    lateinit var lastChosenDateSlotVIew: Button

    inner class DeliverySlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateView: Button = view.date_tv_delSlot

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliverySlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_datepicker_deliveryslot_layout, parent, false)
        return DeliverySlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliverySlotViewHolder, position: Int) {
        holder.dateView.text = dateFormat.format(datesSlots[position])
        if(position == chosenPosition){
            lastChosenDateSlotVIew = holder.dateView
            lastChosenDateSlotVIew.setTextColor(Color.WHITE)
            lastChosenDateSlotVIew.isSelected = true
            chooseDateListener(datesSlots[position], position)
        }

        holder.dateView.setOnClickListener {
           if(this::lastChosenDateSlotVIew.isInitialized){
               lastChosenDateSlotVIew.isSelected = false
               lastChosenDateSlotVIew.setTextColor(Color.parseColor("#A094BA"))

               it.isSelected = true
               lastChosenDateSlotVIew = it as Button
               lastChosenDateSlotVIew.setTextColor(Color.WHITE)

           }
            else{
                lastChosenDateSlotVIew = it as Button
               it.isSelected = true
               lastChosenDateSlotVIew.setTextColor(Color.WHITE)
           }
            chooseDateListener(datesSlots[position], position)

        }
    }

    override fun getItemCount() = datesSlots.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

}