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
import com.example.groceryshoppingapplication.Utils.ProductUnavailabilityDialogGenerator
import com.example.groceryshoppingapplication.Utils.ToastMessageProvider
import com.example.groceryshoppingapplication.adapters.CartItemsAdapter
import com.example.groceryshoppingapplication.enums.ProductAvailability
import com.example.groceryshoppingapplication.viewmodels.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import java.text.DecimalFormat

class CartFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().application)
    }
    private val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels {
        OrderDetailsViewModelFactory(requireActivity().applicationContext)
    }
    var isCheckedForStock = false


    fun checkItemInStock(): Pair<List<String>, List<String>> {
        val decimal = DecimalFormat("#.00")
        val allCartItems = userViewModel.allCartItems.value
        val removedItemsTitle = mutableListOf<String>()
        val removedItemProductCodes = mutableListOf<String>()
        if ((allCartItems?.size ?: 0) > 0) {
            for (item in allCartItems!!) {
                val itemInInventory =
                    inventoryViewModel.getProductDetailsSynchronously(item.productCode)
                if (itemInInventory.productAvailability == ProductAvailability.OUT_OF_STOCK) {
                    userViewModel.removeItemCompletely(item.productCode)
                    removedItemsTitle.add(itemInInventory.brandName+" "+itemInInventory.itemName+" "+decimal.format(itemInInventory.capacity)+" "+ itemInInventory.capacityUnit.value)
                    removedItemProductCodes.add(itemInInventory.productCode.toString())
                }
                else if (itemInInventory.availableQuantity < item.quantity) {
                    for (i in 1..item.quantity - itemInInventory.availableQuantity){
                        userViewModel.removeFromCart(item.productCode)
                    }
                    removedItemsTitle.add(itemInInventory.brandName+" "+itemInInventory.itemName+" "+decimal.format(itemInInventory.capacity)+" "+ itemInInventory.capacityUnit.value)
                    removedItemProductCodes.add(itemInInventory.productCode.toString())
                }
            }
        }
        isCheckedForStock = true
        return Pair(removedItemsTitle, removedItemProductCodes)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val toastMessageProvider = ToastMessageProvider(requireContext())
        userViewModel.allCartItems.observe(this.viewLifecycleOwner) {
            view.cartItems_count.text = StringBuilder().append("(${it.size})")

            var totalPrice = 0.0
            if (it.size > 0) {
                if(!isCheckedForStock){
                    val (unavailableProductTitles, unavailableProductCodes) = checkItemInStock()
                    if (unavailableProductTitles.isNotEmpty()) {
                        ProductUnavailabilityDialogGenerator(
                            layoutInflater,
                            requireContext(),
                            false,
                            unavailableProductTitles,
                            unavailableProductCodes
                        ).buildDialog().show()
                    }
                }
                view.empty_cart_layout.visibility = View.GONE
                view.extFloatingActionButton.visibility = View.VISIBLE
                view.notEmptycart_layout.visibility = View.VISIBLE
                val recyclerView = view.cart_recyclerView

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        viewHolder.itemView.setBackgroundColor(Color.BLACK)
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val cartItem =
                            userViewModel.allCartItems.value!!.get(viewHolder.adapterPosition)
                        userViewModel.removeItemCompletely(cartItem.productCode)
                    }

                }).attachToRecyclerView(recyclerView)
                recyclerView.addOnScrollListener(FabExtendingOnScrollListener(view.extFloatingActionButton))
                val adapter = CartItemsAdapter(
                    it,
                    CartItemTouchListenerImplementation(),
                    toastMessageProvider
                )
                val layoutManager = LinearLayoutManager(this.requireContext())

                recyclerView.layoutManager =  layoutManager
                recyclerView.adapter = adapter
                view.extFloatingActionButton.setOnClickListener { fabView ->
                    orderDetailsViewModel.subTotal = totalPrice
                    orderDetailsViewModel.totalItems = it.size
                    orderDetailsViewModel.userId = userViewModel.currentUser.value?.userId
                    orderDetailsViewModel.mobileNumber = userViewModel.currentUser.value?.mobileNumber
                    if (userViewModel.currentUserAddresses.value?.isEmpty() == true) {
                        val action =
                            CartFragmentDirections.actionCartFragmentToAddressesFragment(true)
                        findNavController().navigate(action)
                    } else {
                        findNavController().navigate(R.id.action_cartFragment_to_deliverySlotFragment)
                    }
                }
                val decimal = DecimalFormat("#.00")
                for (i in it) {
                    totalPrice += (decimal.format(
                        i.quantity * inventoryViewModel.getProductDetailsSynchronously(
                            i.productCode
                        ).unitPrice
                    ).toFloat())
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
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (newState == RecyclerView.SCROLL_STATE_IDLE
            ) {
                recyclerView.adapter?.let {
                    Log.e(TAG,"com vis ${layoutManager.findLastVisibleItemPosition()} vis ${layoutManager.findLastVisibleItemPosition()}")
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == it.itemCount -1)
                        floatingActionButton.extend()

                }
            }
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //scroll up
            if ((dy > 0 || dy < 0) && floatingActionButton.isExtended) {
                floatingActionButton.shrink()
            }

            super.onScrolled(recyclerView, dx, dy)
        }
    }


     inner class CartItemTouchListenerImplementation : CartItemTouchListener{
        override fun addToCart(productCode: Int) {
            userViewModel.addToCart(productCode)
        }

        override fun removeFromCart(productCode: Int) {
            userViewModel.removeFromCart(productCode)
        }

        override fun getCartItemExtraData(productCode: Int): CartItemData {
            val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
            val appendString =
                if (product.capacity > 1 && product.capacityUnit.value.length > 2) "s" else ""

            return CartItemData(
                product.brandName + " " + product.itemName + " " + DecimalFormat("0.#").format(
                    product.capacity
                ) + " " + product.capacityUnit.value + appendString, product.unitPrice
            )
        }

        override fun navigateToProduct(productCode: Int) {
            val action =
                CartFragmentDirections.actionCartFragmentToSingleProductViewFragment(productCode)
            findNavController().navigate(action)
        }
 fun getAvailableQuantity(productCode: Int): Int {
     val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
     return product.availableQuantity

 }
    }


}