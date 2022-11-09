package com.example.groceryshoppingapplication.Utils

import androidx.lifecycle.LiveData
import com.example.groceryshoppingapplication.CartItemData
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.listeners.ProductListTouchListener
import com.example.groceryshoppingapplication.models.OrderedItemEntity
import com.example.groceryshoppingapplication.viewmodels.InventoryViewModel
import com.example.groceryshoppingapplication.viewmodels.ModifyOrderViewModel
import com.example.groceryshoppingapplication.viewmodels.UserViewModel
import java.text.DecimalFormat

abstract class TaskAssigner() : ProductListTouchListener {

    abstract val userViewModelChild: UserViewModel
    abstract val inventoryViewModelChild: InventoryViewModel
    abstract var modifyOrderViewModel: ModifyOrderViewModel


    override fun addToCart(productCode: Int): Boolean {
        if (modifyOrderViewModel.modifiedSessionEnabled) {
            val orderId = modifyOrderViewModel.orderDetail.orderId
            val position = getModifiableItemPositionIfExists(productCode)
            if(position==null){
                modifyOrderViewModel.addToModifiableOrders(
                    OrderedItemEntity(
                        orderId,
                        productCode,
                        1,
                        CodeGeneratorUtil.generateModifiedOrderedItemId(orderId)
                    )
                )
            }
            else
                modifyOrderViewModel.increaseQuantity(position)
            return false

        } else {
            userViewModelChild.addToCart(productCode)
            return true
        }
    }

    private fun getModifiableItemPositionIfExists(productCode: Int): Int? {
        for (i in modifyOrderViewModel.modifiableOrderItems.value!!.indices) {
            if (modifyOrderViewModel.modifiableOrderItems.value!![i].productCode == productCode) {
                return i
            }
        }
        return null
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
                modifyOrderViewModel.decreaseQuantity(getModifiableItemPositionIfExists(productCode)!!)
            }
            return false
        } else {
            userViewModelChild.removeFromCart(productCode)
            return true
        }
    }

    override fun addToWishList(productCode: Int): Boolean {
        val isInWishList = userViewModelChild.checkProductInWishList(productCode).value ?:false

        if (!isInWishList) {
            userViewModelChild.addProductToWishList(productCode)
            return true
        }
        else{
        userViewModelChild.removeFromWishListByProductCode(productCode)
            return false
        }
    }

    override fun removeFromCartCompletely(productCode: Int): Boolean {
        if (modifyOrderViewModel.modifiedSessionEnabled) {
            val item = getModifiableItemIfExists(productCode)
            item?.let {
                if (modifyOrderViewModel.modifiedSessionEnabled) {
                    modifyOrderViewModel.removeFromOrder(it)
                }
            }
            return false
        } else {
            userViewModelChild.removeItemCompletely(productCode)
            return true
        }
    }

    fun getCartItemQuantity(productCode: Int): LiveData<Int>? {
        if (modifyOrderViewModel.modifiedSessionEnabled) {
            return modifyOrderViewModel.getCurrentOrderItemQuantity(productCode)
        } else
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

    override fun navigate(productCode: Int) {
    }

    override fun checkInWishList(productCode:Int):Boolean{
        return userViewModelChild.checkProductInWishList(productCode).value?:false
    }

    override fun checkItemInCart(productCode: Int): Response {
        if (!modifyOrderViewModel.modifiedSessionEnabled) {
            return userViewModelChild.checkItemInCart(productCode)
        } else {
            if (getModifiableItemIfExists(productCode) != null)
                return Response.ITEM_PRESENT_IN_CART
            else
                return Response.NO_SUCH_ITEM_IN_CART
        }
    }


    fun getAvailableQuantity(productCode: Int): Int {
        val product = inventoryViewModelChild.getProductDetailsSynchronously(productCode)
        return product.availableQuantity

    }
}