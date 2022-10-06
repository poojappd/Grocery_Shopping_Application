package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryshoppingapplication.CartItemData
import com.example.groceryshoppingapplication.CartItemTouchListener
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.CartItemsAdapter
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModelFactory
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModelFactory
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cart.view.*

class CartFragment : Fragment() {

    val viewmodel : UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }
    val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().application)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        viewmodel.allCartItems.observe(this.viewLifecycleOwner){
            view.cartItems_count.text = StringBuilder().append("(${it.size})")
            if (it.size > 0){
                view.empty_cart_layout.visibility = View.GONE
                view.extFloatingActionButton.visibility = View.VISIBLE
                view.notEmptycart_layout.visibility = View.VISIBLE
                val cartItemDataList =  mutableMapOf<Int,CartItemData>()
                for(i in it) {
                    inventoryViewModel.getProduct(i.productCode).observe(viewLifecycleOwner) { product ->

                        Log.e(TAG, product.itemName+" "+product.brandName)
                        cartItemDataList.put(product.productCode,
                            CartItemData(
                                product.brandName + " " + product.itemName,
                                product.unitPrice
                            )
                        )

                    val recyclerView = view.cart_recyclerView
                        recyclerView.addOnScrollListener(FabExtendingOnScrollListener(view.extFloatingActionButton))

                        val adapter = CartItemsAdapter(it, CartItemTouchListenerImplementation())
                    recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
                    recyclerView.adapter = adapter
                }

            }
            }
            else{
                view.empty_cart_layout.visibility = View.VISIBLE
                view.extFloatingActionButton.visibility = View.GONE

                view.notEmptycart_layout.visibility = View.GONE

            }

        }
        view.startShopping.setOnClickListener {

            findNavController().navigate(R.id.homePageFragment)
            requireActivity().bottomNavigationView.selectedItemId =R.id.homePageFragment

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
            if(dy<0 && !floatingActionButton.isExtended){
                floatingActionButton.extend()
            }

            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private inner class CartItemTouchListenerImplementation:CartItemTouchListener{
        override fun addToCart(productCode: Int) {
            viewmodel.addToCart(productCode)
        }

        override fun removeFromCart(productCode: Int) {
            viewmodel.removeFromCart(productCode)
        }

        override fun getCartItemExtraData(productCode: Int): CartItemData {
            val product = inventoryViewModel.getProductDetailsSynchronously(productCode)
            return CartItemData(product.itemName +" "+ product.brandName, product.unitPrice)
        }
    }


}