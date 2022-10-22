package com.example.groceryshoppingapplication.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.Utils.CodeGeneratorUtil
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.*
import com.example.groceryshoppingapplication.repositories.CartRepository
import com.example.groceryshoppingapplication.repositories.UserRepository
import com.example.groceryshoppingapplication.repositories.WishListRepository
import kotlinx.coroutines.launch

class UserViewModel(applicationContext: Context) : ViewModel() {

    private val repo = UserRepository(AppDatabase.getDatabase(applicationContext))
    private val myCartRepo = CartRepository(AppDatabase.getDatabase(applicationContext))
    private val myWishListRepo = WishListRepository(AppDatabase.getDatabase(applicationContext))
    private var _currentUser = MutableLiveData<User>()
    private var _currentUserCart = MutableLiveData<CartEntity>()
    private var _currentUserAddresses = MutableLiveData<List<Address>>()
    private var _currentUserOrders = MutableLiveData<List<OrderDetail>>()
    private var _currentUserWishList = MutableLiveData<WishListEntity>()
    private val _isRemovedFromCart = MutableLiveData<Boolean>()
    private val _allCartItems = MutableLiveData<List<CartItemEntity>>()
    private var _cartItemsTotalPrice = MutableLiveData<Double>()
    private val _allWishListItems = MutableLiveData<List<WishListItemEntity>>()


    val isRemovedFromCart: LiveData<Boolean>
        get() = _isRemovedFromCart
    val allCartItems: LiveData<List<CartItemEntity>>
        get() = _allCartItems
    val allWishListItems: LiveData<List<WishListItemEntity>>
        get() = _allWishListItems
    val currentUserAddresses: LiveData<List<Address>>
        get() = _currentUserAddresses


    val currentUser = _currentUser
    val currentUserCart = _currentUserCart
    val currentUserWishList = _currentUserWishList
    val cartItemsTotalPrice = _cartItemsTotalPrice

    fun loginUser(mobileNumber: String): Response {
        val user = repo.loginUser(mobileNumber)
        Log.e(TAG,"****  LOGGED USER ${user.toString()}*****")
        user?.let {
            _currentUser.value = it
            _currentUserCart.value = repo.getUserCartDetails(it.userId).cartEntity
            _currentUserOrders.value = repo.getUserOrderDetails(it.userId)?.let {
                it.ordersInfo
            }
            _currentUserAddresses.value = repo.getUserAddresses(it.userId).addresses
            _currentUserWishList.value = repo.getUserWishListDetails(it.userId).wishListEntity


            viewModelScope.launch {
                refreshCart()
                refreshWishList()
            }
            Log.e(TAG, _allCartItems.value.toString())
            return Response.LOGGED_IN_SUCCESSFULLY
        }
        return Response.NO_SUCH_USER
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repo.updateUser(user)
        }
    }

    fun getCurrentUserData(mobileNumber: String) = repo.loginUser(mobileNumber)


    fun addUserAddress(address: Address) {
        viewModelScope.launch {
            repo.addUserAddress(address)
            refreshUserAddresses()
        }
    }

    fun deleteUserAddress(address: Address) {
        viewModelScope.launch { repo.deleteUserAddress(address) }
        refreshUserAddresses()
    }

    fun updateUserAddress(address: Address) = viewModelScope.launch {
        repo.updateUserAddress(address)
        refreshUserAddresses()
    }

    private fun refreshUserAddresses() {
        _currentUserAddresses.value = repo.getUserAddresses(currentUser.value!!.userId).addresses
    }

    private suspend fun refreshCart() {
        _allCartItems.value =
            myCartRepo.getCartItemsFromCart(currentUserCart.value!!.cartId).cartItemEntity
    }



    fun addToCart(productCode: Int) {
        viewModelScope.launch {
            val items = myCartRepo.getCartItemsFromCart(currentUserCart.value!!.cartId)
            val response = checkItemInCart(productCode)
            Log.e(TAG, response.message)
            if (response == Response.ITEM_PRESENT_IN_CART) {
                for (i in items.cartItemEntity) {
                    if (i.productCode == productCode) {
                        if (i.quantity < 10) {
                            myCartRepo.increaseQuantity(i)
                            currentUserCart.value!!.totalItemsInCart++
                        } else {
                            return@launch
                        }
                        break
                    }
                }
            } else {
                val cartId = currentUserCart.value!!.cartId
                val cartItemId = CodeGeneratorUtil.generateCartItemId(cartId)
                Log.e(TAG, "$cartId $cartItemId ${currentUserCart.value!!.userId}")
                myCartRepo.addToCart(
                    CartItemEntity(
                        productCode,
                        cartId,
                        1,
                        cartItemId
                    )
                )
            }
            refreshCart()
        }

    }

    fun removeFromCart(productCode: Int) {
        viewModelScope.launch {
            val items = myCartRepo.getCartItemsFromCart(currentUserCart.value!!.cartId)
            items.cartItemEntity.forEach {
                Log.e(TAG, "-----------------------------" + "now in cart: ->" + it.toString())
            }

            val response = checkItemInCart(productCode)
            Log.e(TAG, response.message)
            if (response == Response.ITEM_PRESENT_IN_CART) {
                for (i in items.cartItemEntity) {
                    if (i.productCode == productCode) {
                        if (i.quantity > 1) {
                            myCartRepo.decreaseQuantity(i)
                            Log.e(TAG, "***********" + "decreased ->" + i.productCode.toString())

                            currentUserCart.value!!.totalItemsInCart--
                            _isRemovedFromCart.value = false
                            break
                        } else {
                            _isRemovedFromCart.value = true
                            myCartRepo.removeFromCart(i)
                            Log.e(TAG, "***********" + "removed ->" + i.productCode.toString())


                        }
                    }
                }
            }
            refreshCart()
        }

    }

    fun removeItem(productCode: Int) {
        viewModelScope.launch {
            for(i in _allCartItems.value!!) {
                if(i.productCode == productCode)
                    myCartRepo.removeFromCart(i)

                refreshCart()
            }

        }
    }


    fun checkItemInCart(productCode: Int): Response {
        currentUserCart.value?.let {
            val item = myCartRepo.getCartItem(productCode, it.cartId)
            return if (item == null) Response.NO_SUCH_ITEM_IN_CART else Response.ITEM_PRESENT_IN_CART
        }
        return Response.NOT_LOGGED_IN
    }

    fun getCartItemQuantity(productCode: Int): LiveData<Int>? {
        currentUserCart.value?.let {
            val quantity = myCartRepo.getCartItemQuantity(productCode, it.cartId)
            return quantity
        }
        return null
    }

    fun addProductToWishList(productCode: Int) {
        viewModelScope.launch {
            val wishListId = _currentUserWishList.value!!.wishListId

            myWishListRepo.addToWishList(
                WishListItemEntity(
                    productCode, wishListId, CodeGeneratorUtil.generateWishListItemId(wishListId)
                )
            )
            refreshWishList()
        }
    }

    fun removeProductFromWishList(wishListItemId: Int) {
        val wishListId = _currentUserWishList.value!!.wishListId
        viewModelScope.launch {
            myWishListRepo.removeFromWishList(wishListId, wishListItemId)
            refreshWishList()
        }
    }

    private suspend fun refreshWishList() {
        _allWishListItems.value =
            myWishListRepo.getWishListItemsFromWishList(_currentUserWishList.value!!.wishListId).wishListItemEntity
    }

    fun createUser(user: User, cart: CartEntity, wishList: WishListEntity) {
        viewModelScope.launch {
            repo.createUser(user)
            myCartRepo.createCartForNewUser(cart)
            myWishListRepo.createWishList(wishList)
            loginUser(user.mobileNumber)
        }
    }
}

class UserViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return UserViewModel(
            applicationContext
        ) as T
    }
}



