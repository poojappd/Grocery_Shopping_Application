package com.example.groceryshoppingapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_single_product_view.view.*
import java.text.DecimalFormat

class SingleProductViewFragment : Fragment() {

    private val inventoryViewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(requireActivity().applicationContext)
    }
    private val args: SingleProductViewFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_product_view, container, false)
        var imagePathList: Array<String>
        val decimal = DecimalFormat("0.#")
        inventoryViewModel.getProduct(args.displayProductCode).observe(viewLifecycleOwner) {
            val productCode = it.productCode
            imagePathList =
                requireActivity().applicationContext.assets.list("product_images/${productCode.toString()}") as Array<String>
            view.apply {
                product_title_tv.text = it.brandName + " - " + it.itemName
                val capacityValue = it.capacity
                val appendString = if (capacityValue>1) "s" else ""
                product_price_tv.text = decimal.format(it.unitPrice)
                quantity_value_tv.text = decimal.format(capacityValue)+ " " +it.capacityUnit.value+appendString
                product_description_tv.text = it.itemDescription
                product_image_viewPager.adapter = ProductViewPagerAdapter(it.productCode,imagePathList)
                product_image_viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                circle_indicator.setViewPager(product_image_viewPager)
            }

            Log.e(TAG, "inside - "+imagePathList.size.toString())

        }

        return view
    }



}

