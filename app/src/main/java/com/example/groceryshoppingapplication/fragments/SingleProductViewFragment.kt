package com.example.groceryshoppingapplication.fragments

import android.app.ProgressDialog.show
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
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
import com.example.groceryshoppingapplication.R
import com.example.groceryshoppingapplication.Utils.ProductUnavailabilityDialogGenerator
import com.example.groceryshoppingapplication.Utils.ToastMessageProvider
import com.example.groceryshoppingapplication.adapters.ProductViewPagerAdapter
import com.example.groceryshoppingapplication.enums.ProductAvailability
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
        val toastMessageProvider = ToastMessageProvider(requireContext())

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

                if(it.productAvailability == ProductAvailability.OUT_OF_STOCK){
                    productUnAvailableLayout(view, true)

                }
                else{
                    productUnAvailableLayout(view, false)

                }
                userViewmodel.getCartItemQuantity(it.productCode)
                    ?.observe(viewLifecycleOwner) { qty ->
                        if (qty != null) {
                            val productAlertDialog =
                                ProductUnavailabilityDialogGenerator(layoutInflater, context, true)
                            if (it.productAvailability == ProductAvailability.OUT_OF_STOCK) {
                                productAlertDialog.buildDialog().show()
                                userViewmodel.removeItemCompletely(it.productCode)
                            } else if (it.availableQuantity < qty) {
                                productAlertDialog.buildDialog().show()
                                while (qty == it.availableQuantity) {
                                    userViewmodel.removeFromCart(it.productCode)
                                }

                            } else {
                                view.itemCount_textView_singleProductView.text = qty.toString()
                                toggleAddToCartVisibility(true)
                                increaseQuantity_single_product.setOnClickListener { buttonview ->
                                    if (qty < it.availableQuantity) {
                                        if(qty<=5){
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
                                            toastMessageProvider.show("Maximum ordering quantity is 5")
                                        }

                                    } else {
                                         toastMessageProvider.show("Only ${it.availableQuantity} left")
                                    }
                                }



                                decreaseQuantity_single_product.setOnClickListener { buttonView ->
                                    userViewmodel.currentUserCart.value?.cartId?.let { id ->
                                        if (qty == 1)
                                            toastMessageProvider.show("Item removed from cart")

                                        userViewmodel.removeFromCart(it.productCode)
                                        Log.e(
                                            TAG,
                                            userViewmodel.getCartItemQuantity(it.productCode)
                                                .toString()
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
                    toastMessageProvider.show("Added to cart")

                }
                saveForLater_single_product_view.setOnClickListener { buttonView ->

                    userViewmodel.checkProductInWishList(productCode)
                        .observe(viewLifecycleOwner) { isInWishList ->
                            if (!isInWishList) {
                                userViewmodel.addProductToWishList(it.productCode)
                                toastMessageProvider.show("Added to wishList!")
                                buttonView.saveForLater_textView.text = "Saved"

                            } else {
                                userViewmodel.removeFromWishListByProductCode(it.productCode)
                                buttonView.saveForLater_textView.text = "Save for later"
                                toastMessageProvider.show("Removed From wishList!")
                            }
                        }
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

    fun productUnAvailableLayout(view: View, isUnavailable: Boolean) {
        if (isUnavailable) {
            view.notAvailableBAnner.visibility = View.VISIBLE
            notAvailableButtonBanner.visibility = View.VISIBLE
        } else {
            view.notAvailableBAnner.visibility = View.GONE
            notAvailableButtonBanner.visibility = View.GONE
        }
    }

}

