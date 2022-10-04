package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.groceryshoppingapplication.adapters.ProductViewPagerAdapter
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.fragment_single_product_view.*
import kotlinx.android.synthetic.main.fragment_single_product_view.view.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class SingleProductViewFragment : Fragment() {

    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    private val args: SingleProductViewFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_product_view, container, false)
        var imagePathList: Array<String>
        val decimal = DecimalFormat("0.##")

        inventoryViewModel.getProduct(args.displayProductCode).observe(viewLifecycleOwner) {
            val productCode = it.productCode
            imagePathList =
                requireActivity().applicationContext.assets.list("product_images/${productCode.toString()}") as Array<String>
            view.apply {
                product_title_tv.text = it.brandName + " - " + it.itemName
                val capacityValue = it.capacity
                val appendString = if (capacityValue > 1 && it.capacityUnit.value.length > 2) "s" else ""
                product_price_tv.text = decimal.format(it.unitPrice)
                quantity_value_tv.text =
                    decimal.format(capacityValue) + " " + it.capacityUnit.value + appendString
                product_description_tv.text = it.itemDescription
                product_image_viewPager.adapter =
                    ProductViewPagerAdapter(it.productCode, imagePathList)
                product_image_viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                circle_indicator.setViewPager(product_image_viewPager)


                userViewmodel.getCartItemQuantity(it.productCode)?.observe(viewLifecycleOwner){ qty->
                    if(qty!=null) {
                        view.itemCount_textView_singleProductView.text = qty.toString()
                        toggleAddToCartVisibility(true)

                        increaseQuantity_single_product.setOnClickListener { buttonview ->

                            userViewmodel.currentUserCart.value?.cartId?.let { id ->

                                    userViewmodel.addToCart(it.productCode)
                                    Log.e(TAG, userViewmodel.getCartItemQuantity(it.productCode).toString())
                                    Toast.makeText(this@SingleProductViewFragment.requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()



                            }

                        }
                        decreaseQuantity_single_product.setOnClickListener { buttonView->
                            userViewmodel.currentUserCart.value?.cartId?.let { id ->

                                    userViewmodel.removeFromCart(it.productCode)
                                    Log.e(TAG, userViewmodel.getCartItemQuantity(it.productCode).toString())

                                            Toast.makeText(
                                                this@SingleProductViewFragment.requireContext(),
                                                "Removed from cart ${it.brandName}",
                                                Toast.LENGTH_SHORT
                                            ).show()


                            }
                        }

                    }
                    else{
                        toggleAddToCartVisibility(false)

                    }


                }


                //addTocartButtonListener
                addTocart_Toggle_visibility.setOnClickListener { buttonView->
                    toggleAddToCartVisibility(true)
                    userViewmodel.addToCart(it.productCode)

                }


            //addTocartButtonListener
            }
        }


        return view
    }

    fun toggleAddToCartVisibility(inCart:Boolean){
        if(!inCart){
            addTocart_Toggle_visibility.visibility = View.VISIBLE
            increaseQuantity_Toggle_visibility.visibility = View.GONE
        }
        else{
            addTocart_Toggle_visibility.visibility = View.GONE
            increaseQuantity_Toggle_visibility.visibility = View.VISIBLE

        }
    }


}

