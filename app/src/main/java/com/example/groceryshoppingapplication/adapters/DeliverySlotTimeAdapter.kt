package com.example.groceryshoppingapplication.adapters

import android.content.ContentValues
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.R
import kotlinx.android.synthetic.main.custom_timepicker_deliveryslot_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class DeliverySlotTimeAdapter(private val timeSlots: List<Date>,
                              private val chooseTimeListener: (Date,Int)->Unit,
                              private val chosenPosition:Int? = null) :
    RecyclerView.Adapter<DeliverySlotTimeAdapter.DeliverySlotTimeViewHolder>() {

    private val timeFormat = SimpleDateFormat("hh a", Locale.getDefault())
    lateinit var lastChosenTimeSlotVIew: Button


    inner class DeliverySlotTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeView = view.time_delSlot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliverySlotTimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_timepicker_deliveryslot_layout, parent, false)
        Log.e(ContentValues.TAG,"INSIDE TIME ADAPTER----${chosenPosition}")

        return DeliverySlotTimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliverySlotTimeViewHolder, position: Int) {
        holder.timeView.text = timeFormat.format(timeSlots[position])
        Log.e(ContentValues.TAG,"TIME CHOOSEN POSITION----${chosenPosition}")

        if(position == chosenPosition){
            lastChosenTimeSlotVIew = holder.timeView
            lastChosenTimeSlotVIew.setTextColor(Color.WHITE)
            lastChosenTimeSlotVIew.isSelected = true
            chooseTimeListener(timeSlots[position],position)
        }
        holder.timeView.setOnClickListener {
            if(this::lastChosenTimeSlotVIew.isInitialized){
                lastChosenTimeSlotVIew.isSelected = false
                lastChosenTimeSlotVIew.setTextColor(Color.parseColor("#A094BA"))

                it.isSelected = true
                lastChosenTimeSlotVIew = it as Button
                lastChosenTimeSlotVIew.setTextColor(Color.WHITE)

            }
            else{
                lastChosenTimeSlotVIew = it as Button
                it.isSelected = true
                lastChosenTimeSlotVIew.setTextColor(Color.WHITE)
            }
            chooseTimeListener(timeSlots[position],position)

        }
    }

    override fun getItemCount() = timeSlots.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position


}