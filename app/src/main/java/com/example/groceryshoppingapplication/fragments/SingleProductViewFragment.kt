package com.example.groceryshoppingapplication.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.groceryshoppingapplication.adapters.AdvertisementViewPager
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.adapters.ProductViewPagerAdapter
import com.example.groceryshoppingapplication.viewmodels.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_single_product_view.*
import kotlinx.android.synthetic.main.fragment_single_product_view.view.*
import java.text.DecimalFormat

class SingleProductViewFragment : Fragment() {

    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    val userViewmodel: UserViewModel by activityViewModels {
        UserViewModelFactory(requireActivity().applicationContext)
    }

    private val args: SingleProductViewFragmentArgs by navArgs()

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
        val view = inflater.inflate(R.layout.fragment_single_product_view, container, false)
        view.single_product_toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        var imagePathList: Array<String>
        val decimal = DecimalFormat("0.##")

        inventoryViewModel.getProduct(args.displayProductCode).observe(viewLifecycleOwner) {
            val productCode = it.productCode
            imagePathList =
                requireActivity().applicationContext.assets.list("product_images/${productCode.toString()}") as Array<String>
            view.apply {
                    product_title_tv.text = StringBuilder().append(it.brandName + " - " + it.itemName)
                val capacityValue = it.capacity
                val appendString =
                    if (capacityValue > 1 && it.capacityUnit.value.length > 2) "s" else ""
                product_price_tv.text = decimal.format(it.unitPrice)
                quantity_value_tv.text = StringBuilder().append(
                    decimal.format(capacityValue) + " " + it.capacityUnit.value + appendString
                ).toString()
                product_description_tv.text = it.itemDescription
                product_image_viewPager.adapter =
                    ProductViewPagerAdapter(it.productCode, imagePathList)
                product_image_viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                circle_indicator.setViewPager(product_image_viewPager)


                userViewmodel.getCartItemQuantity(it.productCode)
                    ?.observe(viewLifecycleOwner) { qty ->
                        if (qty != null) {
                            view.itemCount_textView_singleProductView.text = qty.toString()
                            toggleAddToCartVisibility(true)
                            val minQtyExceededToast = Toast.makeText(
                                this@SingleProductViewFragment.requireContext(),
                                "Maximum order quantity is 10",
                                Toast.LENGTH_SHORT
                            )
                            increaseQuantity_single_product.setOnClickListener { buttonview ->
                                if (qty < 10) {
                                    userViewmodel.currentUserCart.value?.cartId?.let { id ->
                                        userViewmodel.addToCart(it.productCode)
                                        Log.e(
                                            TAG,
                                            userViewmodel.getCartItemQuantity(it.productCode)
                                                .toString()
                                        )
                                    }
                                }

                                else{
                                    minQtyExceededToast.show()
                                }

                            }
                            decreaseQuantity_single_product.setOnClickListener { buttonView ->
                                userViewmodel.currentUserCart.value?.cartId?.let { id ->
                                    if (qty == 1) {
                                        Toast.makeText(
                                            this@SingleProductViewFragment.requireContext(),
                                            "Removed from cart ${it.brandName}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    userViewmodel.removeFromCart(it.productCode)
                                    Log.e(
                                        TAG,
                                        userViewmodel.getCartItemQuantity(it.productCode).toString()
                                    )
                                    }

                                }
                            }

                        } else {
                            toggleAddToCartVisibility(false)

                        }


                    }


                //addTocartButtonListener
                addTocart_Toggle_visibility.setOnClickListener { buttonView ->
                    toggleAddToCartVisibility(true)
                    userViewmodel.addToCart(it.productCode)
                    Toast.makeText(
                        this@SingleProductViewFragment.requireContext(),
                        "Added to cart",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                saveForLater_single_product_view.setOnClickListener { buttonView ->
                    userViewmodel.addProductToWishList(it.productCode)
                    Toast.makeText(requireContext(), "Added to wishList! ", Toast.LENGTH_SHORT)
                        .show()
                }


                //addTocartButtonListener
            }
        }


        return view
    }

    fun toggleAddToCartVisibility(inCart: Boolean) {
        if (!inCart) {
            addTocart_Toggle_visibility.visibility = View.VISIBLE
            increaseQuantity_Toggle_visibility.visibility = View.GONE
        } else {
            addTocart_Toggle_visibility.visibility = View.GONE
            increaseQuantity_Toggle_visibility.visibility = View.VISIBLE

        }
    }


}

