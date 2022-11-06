package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.LinkAddress
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.DateTimeFormatter.formatDate
import com.example.groceryshoppingapplication.adapters.DeliverySlotDateAdapter
import com.example.groceryshoppingapplication.adapters.DeliverySlotTimeAdapter
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_delivery_slot.view.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*
import kotlinx.android.synthetic.main.fragment_product_search.*
import java.text.SimpleDateFormat
import java.util.*

class DeliverySlotFragment : Fragment() {

    val userViewModel:UserViewModel by activityViewModels {
        UserViewModelFactory(requireContext().applicationContext)
    }
    val deliverySlotViewModel: DeliverySlotViewModel by viewModels {
        DeliverySlotViewModelFactory(requireContext().applicationContext)
    }
    val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels {
        OrderDetailsViewModelFactory(requireActivity().applicationContext)
    }
    val modifyOrderViewModel:ModifyOrderViewModel by activityViewModels {
        ModifyOrderViewModelFactory(requireContext().applicationContext)
    }
    lateinit var mainView:View

    override fun onResume() {
        requireActivity().bottomNavigationView.visibility = View.GONE
        super.onResume()
        val view = mainView
        val toolbar = view.deliverySlot_Toolbar
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        val dateArray = getDateArray()
        var chosenTime:Date? = null

        val addressView = view.address_delSLot
        userViewModel.refreshUserAddresses()
        userViewModel.currentUserChosenAddress.observe(viewLifecycleOwner) {
            val currentAddress = it
            addressView.text = StringBuilder().append(
                "${currentAddress.houseNo}, ${currentAddress.streetDetails}, ${currentAddress.areaDetails}, ${currentAddress.city} - ${currentAddress.pincode}"
            )
        }


        val dateRecycerView = view.dateChoose_rv
        val timeRv = view.timeChoose_rv
        deliverySlotViewModel.timePosition?.let {
            view.invisible_time_picker.visibility = View.VISIBLE
        }

        dateRecycerView.adapter = DeliverySlotDateAdapter(dateArray, { date: Date, position:Int ->
            deliverySlotViewModel.chosenDate = date
            deliverySlotViewModel.datePosition?.let {
                if(it!= position){
                    Log.e(TAG,"NOT EQUAL LINE EXECUTING")

                    deliverySlotViewModel.chosenTime = null
                    deliverySlotViewModel.timePosition = null
                    view.continue_button_deliverySlot.visibility = View.GONE
                    view.chosenSlotTV.visibility = View.INVISIBLE
                }
            }
            deliverySlotViewModel.datePosition = position

            val timeArray = getTimeArray(deliverySlotViewModel.chosenDate!!)
            Log.e(TAG,"TIME CHOOSEN  DATE${date.time}, ${deliverySlotViewModel.timePosition}")
            val tlv = GridLayoutManager(context,1)
            timeRv.layoutManager = tlv
            Log.e(TAG,"LINE BELOW TIME CHOSEN DATE, ${deliverySlotViewModel.timePosition}")

            timeRv.adapter = DeliverySlotTimeAdapter(timeArray,{time: Date, pos:Int->
                Log.e(TAG,"TIME CHOOSEN TIME${date.time}")

                deliverySlotViewModel.chosenTime = time
                deliverySlotViewModel.timePosition = pos
                view.continue_button_deliverySlot.visibility = View.VISIBLE
                view.delSlot_Scrollview.post {
                    view.delSlot_Scrollview.fullScroll(View.FOCUS_DOWN)
                }
                view.chosenSlotTV.text = StringBuilder().append(date.formatDate("dd EEEE")+" "+time.formatDate("hh a"))
                view.chosenSlotTV.visibility = View.VISIBLE
            },deliverySlotViewModel.timePosition)
            view.invisible_time_picker.visibility = View.VISIBLE

            val timeSNapHelper = PagerSnapHelper()
            timeRv.setOnFlingListener(null);
            timeSNapHelper.attachToRecyclerView(timeRv)

        },deliverySlotViewModel.datePosition)
        val lm = LinearLayoutManager(context)
        dateRecycerView.layoutManager = lm
        val dateSnapHelper = MyPagerSnapHelper()
        dateRecycerView.setOnFlingListener(null);

        dateSnapHelper.attachToRecyclerView(dateRecycerView)
        view.materialButton2.setOnClickListener {
            val action = DeliverySlotFragmentDirections.actionDeliverySlotFragmentToAddressesFragment(skipToDeliverySlot = true)
            findNavController().navigate(action)
        }
        view.continue_button_deliverySlot.setOnClickListener {
            userViewModel.currentUserAddresses.observe(viewLifecycleOwner){
                orderDetailsViewModel.deliverySlot = deliverySlotViewModel.chosenTime
                orderDetailsViewModel.deliveringAddress = it.get(0)
                userViewModel.currentUser.observe(viewLifecycleOwner){
                    orderDetailsViewModel.receiverName = it.firstName+" "+ it.lastName
                }
            }
            if (modifyOrderViewModel.modifiedSessionEnabled)
                findNavController().navigate(R.id.action_deliverySlotFragment_to_modifiedPlaceOrderFragment)
            else
                findNavController().navigate(R.id.action_deliverySlotFragment_to_placeOrderFragment)
        }

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
        val view = inflater.inflate(R.layout.fragment_delivery_slot, container, false)
        mainView = view
            return view
        }
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
        Log.e(TAG,"INSIDE TIME ARRAY POPULATION")
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
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23 )
        calendarEnd.set(Calendar.MINUTE, 0 )

        val timeArray = mutableListOf<Date>()
        while (calendarStart.before(calendarEnd)) {
            timeArray.add(calendarStart.time)
            calendarStart.add(Calendar.HOUR, 1)
        }
        return timeArray
    }



class MyPagerSnapHelper: PagerSnapHelper() {

    fun smoothScrollToPosition(layoutManager: RecyclerView.LayoutManager, position: Int) {
        val smoothScroller = createScroller(layoutManager) ?: return

        smoothScroller.targetPosition = position
        layoutManager.startSmoothScroll(smoothScroller)
    }
}
