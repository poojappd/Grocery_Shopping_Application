package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.CartItemData
import com.example.groceryshoppingapplication.listeners.CartItemTouchListener
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.CartItemsAdapter
import com.example.groceryshoppingapplication.viewmodels.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import java.text.DecimalFormat

class CartFragment : Fragment() {

    private val viewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().application)
    }
    private val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels {
        OrderDetailsViewModelFactory(requireActivity().applicationContext)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        viewmodel.allCartItems.observe(this.viewLifecycleOwner) {
            view.cartItems_count.text = StringBuilder().append("(${it.size})")

            var totalPrice = 0.0
            if (it.size > 0) {
                view.empty_cart_layout.visibility = View.GONE
                view.extFloatingActionButton.visibility = View.VISIBLE
                view.notEmptycart_layout.visibility = View.VISIBLE
                val recyclerView = view.cart_recyclerView

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT){
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {

                        viewHolder.itemView.setBackgroundColor(Color.BLACK)
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                       val cartItem =  viewmodel.allCartItems.value!!.get(viewHolder.adapterPosition)
                        viewmodel.removeFromCart(cartItem.productCode)
                    }

                }).attachToRecyclerView(recyclerView)
                recyclerView.addOnScrollListener(FabExtendingOnScrollListener(view.extFloatingActionButton))
                val adapter = CartItemsAdapter(
                    it,
                    CartItemTouchListenerImplementation()
                ) { price: Double -> totalPrice = price }
                recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
                recyclerView.adapter = adapter
                view.extFloatingActionButton.setOnClickListener { fabView->
                    orderDetailsViewModel.subTotal = totalPrice
                    orderDetailsViewModel.totalItems = it.size
                    orderDetailsViewModel.userId = viewmodel.currentUser.value?.userId
                    orderDetailsViewModel.mobileNumber = viewmodel.currentUser.value?.mobileNumber
                    if(viewmodel.currentUserAddresses.value?.isEmpty() == true){
                        val action = CartFragmentDirections.actionCartFragmentToAddressesFragment(true)
                        findNavController().navigate(action)
                    }
                    else {
                        findNavController().navigate(R.id.action_cartFragment_to_deliverySlotFragment)
                    }
                }
                val decimal = DecimalFormat("#.00")
                for(i in it){
                    totalPrice  += (decimal.format(i.quantity * inventoryViewModel.getProductDetailsSynchronously(i.productCode).unitPrice).toFloat())
                }
                view.totalPrice_cart_fragment.text = decimal.format(totalPrice)

            } else {
                view.empty_cart_layout.visibility = View.VISIBLE
                view.extFloatingActionButton.visibility = View.GONE

                view.notEmptycart_layout.visibility = View.GONE

            }
        }
        view.startShopping.setOnClickListener {

            findNavController().navigate(R.id.homePageFragment)
            requireActivity().bottomNavigationView.selectedItemId = R.id.homePageFragment

        }
        return view
    }

    private class FabExtendingOnScrollListener(
        private val floatingActionButton: ExtendedFloatingActionButton
    ) : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                && !floatingActionButton.isExtended

            ) {
                floatingActionButton.extend()
            }
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 && floatingActionButton.isExtended) {
                floatingActionButton.shrink()
            }
            if (dy < 0 && !floatingActionButton.isExtended) {
                floatingActionButton.extend()
            }

            super.onScrolled(recyclerView, dx, dy)
        }
    }


    private inner class CartItemTouchListenerImplementation : CartItemTouchListener {
        override fun addToCart(productCode: Int) {
            viewmodel.addToCart(productCode)
        }

        override fun removeFromCart(productCode: Int) {
            viewmodel.removeFromCart(productCode)
        }

        override fun getCartItemExtraData(productCode: Int): CartItemData {
            val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
            return CartItemData( product.brandName+ " "+ product.itemName, product.unitPrice )
        }

    }


}