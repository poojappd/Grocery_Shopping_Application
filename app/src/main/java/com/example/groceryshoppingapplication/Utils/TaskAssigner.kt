package com.example.groceryshoppingapplication.Utils

import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.groceryshoppingapplication.CartItemData
import com.example.groceryshoppingapplication.fragments.CartFragmentDirections
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.ModifyOrderViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import java.text.DecimalFormat

abstract class TaskAssigner() {

    abstract val userViewModelChild: UserViewModel
    abstract val inventoryViewModelChild: InventoryViewModel
    abstract var modifyOrderViewModel: ModifyOrderViewModel

    //Jesus be with me
    fun addToCart(productCode: Int): Boolean {
        if (modifyOrderViewModel.modifiedSessionEnabled) {
            val orderId = modifyOrderViewModel.orderDetail.orderId

            modifyOrderViewModel.addToModifiableOrders(
                OrderedItemEntity(
                    orderId,
                    productCode,
                    1,
                    CodeGeneratorUtil.generateOrderedItemId(orderId)
                )
            )
            return false
        } else {
            userViewModelChild.addToCart(productCode)
            return true
        }
    }

    private fun getModifiableItemIfExists(productCode: Int): OrderedItemEntity? {
        for (i in modifyOrderViewModel.modifiableOrderItems.value!!) {
            if (i.productCode == productCode) {
                return i
            }
        }
        return null
    }

    fun removeFromCart(productCode: Int): Boolean {
        if (modifyOrderViewModel.modifiedSessionEnabled) {
            val item = getModifiableItemIfExists(productCode)
            item?.let {
                if (modifyOrderViewModel.modifiedSessionEnabled) {
                    modifyOrderViewModel.removeFromOrder(it, true)
                }
                userViewModelChild.removeFromCart(productCode)
            }
            return false
        } else {
            userViewModelChild.removeFromCart(productCode)
            return true
        }
    }

    fun getCartItemQuantity(productCode: Int): LiveData<Int>? {
        if (modifyOrderViewModel.modifiedSessionEnabled) {
            return modifyOrderViewModel.getCurrentOrderItemQuantity(productCode)
        } else {
            return userViewModelChild.getCartItemQuantity(productCode)

        }

        fun getCartItemExtraData(productCode: Int): CartItemData {
            val product = inventoryViewModelChild.getProductDetailsSynchronously(productCode)
            val appendString =
                if (product.capacity > 1 && product.capacityUnit.value.length > 2) "s" else ""

            return CartItemData(
                product.brandName + " " + product.itemName + " " + DecimalFormat("0.#").format(
                    product.capacity
                ) + " " + product.capacityUnit.value + appendString, product.unitPrice
            )
        }


        fun getAvailableQuantity(productCode: Int): Int {
            val product = inventoryViewModelChild.getProductDetailsSynchronously(productCode)
            return product.availableQuantity

        }
    }