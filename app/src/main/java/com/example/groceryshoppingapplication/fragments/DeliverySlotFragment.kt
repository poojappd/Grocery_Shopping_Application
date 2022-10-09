package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.net.LinkAddress
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.DeliverySlotDateAdapter
import com.example.groceryshoppingapplication.adapters.DeliverySlotTimeAdapter
import com.example.groceryshoppingapplication.viewmodels.DeliverySlotViewModel
import com.example.groceryshoppingapplication.viewmodels.DeliverySlotViewModelFactory
import kotlinx.android.synthetic.main.fragment_delivery_slot.view.*
import java.text.SimpleDateFormat
import java.util.*

class DeliverySlotFragment : Fragment() {

    val deliverySlotViewModel: DeliverySlotViewModel by activityViewModels {
        DeliverySlotViewModelFactory(requireContext().applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delivery_slot, container, false)
        val dateArray = getDateArray()
        var chosenDate: Date
        var chosenTime = Date()


        val dateRecycerView = view.dateChoose_rv
        dateRecycerView.adapter = DeliverySlotDateAdapter(dateArray) { date: Date ->
            chosenDate = date
           val timeArray = getTimeArray(chosenDate)

            view.timeChoose_rv.adapter = DeliverySlotTimeAdapter(timeArray){time: Date->
                chosenTime = time
            view.continue_button_deliverySlot.visibility = View.VISIBLE
            }
            view.invisible_time_picker.visibility = View.VISIBLE
            view.timeChoose_rv.layoutManager = LinearLayoutManager(context)

        }
        dateRecycerView.layoutManager = LinearLayoutManager(context)
        view.continue_button_deliverySlot.setOnClickListener {
                deliverySlotViewModel.chosenTime = chosenTime
            Log.e(TAG, chosenTime.toString())

        }
        return view
    }

    private fun getDateArray(): MutableList<Date> {
        val start = Date()
        val end = Date()
        val calendarStart = Calendar.getInstance()
        calendarStart.time = start
        if(getTimeArray(Date()).isEmpty())
                calendarStart.add(Calendar.DATE,1)


        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = end
        calendarEnd.add(Calendar.DAY_OF_MONTH, 7)
        val dateArray = mutableListOf<Date>()
        while (calendarStart.before(calendarEnd)) {
            dateArray.add(calendarStart.time)
            calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dateArray
    }

    private fun getTimeArray(chosenDate:Date): List<Date> {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        if(chosenDate.date == Date().date) {
            calendarStart.time = Date()
            calendarStart.add(Calendar.HOUR, 1)
        }
        else{
            calendarStart.time = chosenDate
            calendarEnd.time = chosenDate
            calendarStart.set(Calendar.HOUR_OF_DAY,6)
        }
        calendarStart.set(Calendar.MINUTE,0)
        calendarEnd.set(Calendar.HOUR, 12 )
        calendarEnd.set(Calendar.MINUTE, 0 )

        val timeArray = mutableListOf<Date>()
        while (calendarStart.before(calendarEnd)) {
            timeArray.add(calendarStart.time)
            calendarStart.add(Calendar.HOUR, 1)
        }
        return timeArray
    }



}