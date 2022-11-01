package com.example.groceryshoppingapplication.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.MyGroceryApplication
import com.example.groceryshoppingapplication.adapters.OrdersAdapter
import com.example.groceryshoppingapplication.enums.OrderStatus
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_orders_view_pager.view.*


class OrdersViewPagerFragment() : Fragment() {
    private lateinit var applicationContext: Context
constructor(applicationContext: Context) : this() {
    this.applicationContext = applicationContext
}

    override fun onResume() {
        requireActivity().bottomNavigationView.visibility = View.GONE
        super.onResume()
        Log.e(ContentValues.TAG, "ONResumE CART FRAGMENT")

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
        if(!this::applicationContext.isInitialized)
            applicationContext = requireActivity().applicationContext
        val view = inflater.inflate(R.layout.fragment_orders_view_pager, container, false)
        view.toolbar_myOrders.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        val userId = MyGroceryApplication.preferences.getString("loggedUserId", null)!!

        if (applicationContext!=null) {

            val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
                OrderHistoryViewModelFactory(applicationContext)
            }
            orderHistoryViewModel.refreshMyOrders(userId)
            orderHistoryViewModel.allOrders.observe(viewLifecycleOwner) {
                if(it.size>0) {
                    val ordersRecyclerView = view.myOrders_recyclerView_viewPager
                    ordersRecyclerView.visibility = View.VISIBLE
                    view.empty_orders_layout.visibility = View.GONE
                    ordersRecyclerView.adapter = OrdersAdapter(it,{orderId:String ->
                        val action = OrdersViewPagerFragmentDirections.actionOrdersViewPagerFragmentToOrderSummaryFragment(orderId)
                        findNavController().navigate(action)
                    }) { orderId: String, isComplete: Boolean -> if (isComplete) orderHistoryViewModel.updateOrderStatus(OrderStatus.COMPLETE, orderId) }
                    ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
                else{
                    view.myOrders_recyclerView_viewPager.visibility = View.GONE
                    view.empty_orders_layout.visibility = View.VISIBLE
                    val targetView = view.empty_bag_image
                    view.startShopping_orders.setOnClickListener {

                        findNavController().navigate(R.id.homePageFragment)
                        requireActivity().bottomNavigationView.selectedItemId = R.id.homePageFragment

                    }
                    val moveUp: ObjectAnimator =
                        ObjectAnimator.ofFloat(targetView, "translationY", -200f)
                    moveUp.setRepeatCount(0)
                    moveUp.setDuration(1000)

                    val rotateClockWise = ObjectAnimator.ofFloat(targetView,"rotation",180f)
                    rotateClockWise.repeatCount = 0
                    rotateClockWise.duration = 1000

                    val rotateAntiClockWise = ObjectAnimator.ofFloat(targetView,"rotation",0f)
                    rotateAntiClockWise.repeatCount = 0
                    rotateAntiClockWise.duration = 1000

                    val moveDown = ObjectAnimator.ofFloat(targetView, "translationY", 0f)
                    moveDown.repeatCount = 0
                    moveDown.duration = 1000


                    val set = AnimatorSet()
                    set.play(moveUp).before(rotateClockWise)
                    set.play(rotateClockWise).before(rotateAntiClockWise)
                    set.play(rotateAntiClockWise).before(moveDown)
                    set.start()

                }
            }
        }

        return view
    }




}