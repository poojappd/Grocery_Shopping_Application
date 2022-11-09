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
    private var _currentUserDefaultAddress = MutableLiveData<Address>()
    private var _currentUserOrders = MutableLiveData<List<OrderDetail>>()
    private var _currentUserWishList = MutableLiveData<WishListEntity>()
    private val _isRemovedFromCart = MutableLiveData<Boolean>()
    private val _allCartItems = MutableLiveData<List<CartItemEntity>>()
    private var _cartItemsTotalPrice = MutableLiveData<Double>()
    private val _allWishListItems = MutableLiveData<List<WishListItemEntity>>()
    private var chosenAddress = MutableLiveData<Address>()
    private var chosenAddressPosition = MutableLiveData<Int>()
    private var _recentSearches = MutableLiveData<List<String>>()

    val isRemovedFromCart: LiveData<Boolean>
        get() = _isRemovedFromCart
    val allCartItems: LiveData<List<CartItemEntity>>
        get() = _allCartItems
    val allWishListItems: LiveData<List<WishListItemEntity>>
        get() = _allWishListItems
    val currentUserAddresses: LiveData<List<Address>>
        get() = _currentUserAddresses
    val currentUserDefaultAddress: LiveData<Address>
        get() = _currentUserDefaultAddress
    val currentUserChosenAddress: LiveData<Address>
        get() = chosenAddress
    val currentUserChosenAddressPosition: LiveData<Int>
        get() = chosenAddressPosition
    val recentSearches: LiveData<List<String>>
        get() = _recentSearches


    val currentUser = _currentUser
    val currentUserCart = _currentUserCart
    val currentUserWishList = _currentUserWishList
    val cartItemsTotalPrice = _cartItemsTotalPrice

    fun loginUser(mobileNumber: String): Response {
        val user = repo.loginUser(mobileNumber)
        user?.let {
            _currentUser.value = it
            _currentUserCart.value = repo.getUserCartDetails(it.userId).cartEntity
            _currentUserOrders.value = repo.getUserOrderDetails(it.userId)?.let {
                it.ordersInfo
            }
            _currentUserAddresses.value = repo.getUserAddresses(it.userId).addresses
            _currentUserWishList.value = repo.getUserWishListDetails(it.userId).wishListEntity

            repo.getDefaultAddressId(it.userId)?.let { defAddress ->
                val address = repo.getAddress(defAddress.addressId)
                _currentUserDefaultAddress.value = address
                chosenAddress.value = address
                chosenAddressPosition.value = 0
            }


            viewModelScope.launch {
                refreshCart()
                refreshWishList()
            }
            return Response.LOGGED_IN_SUCCESSFULLY
        }
        return Response.NO_SUCH_USER
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repo.updateUser(user)
        }
    }

    fun addToRecentSearch(query: String) {
        if (!containedInRecentSearch(query)) {
            var size = _recentSearches.value?.size ?: 0
            if (size == 0) _recentSearches.value = listOf()
            if (size > 6) {
                _recentSearches.value = _recentSearches.value!!.toMutableList().apply {
                    removeAt(0)
                    size--

                }
            }
            _recentSearches.value = _recentSearches.value!!.toMutableList().apply {
                add(size, query)
            }
        }
    }

    fun containedInRecentSearch(query: String): Boolean {
        var size = _recentSearches.value?.size ?: 0
        if (size == 0)
            return false
        return (_recentSearches.value!!.contains(query))
    }


    fun updateDefaultAddress(addressId: String) {
        viewModelScope.launch {
            val newDefaultAddress = repo.getAddress(addressId)
            val currentDefaultAddress = repo.getDefaultAddressId(currentUser.value!!.userId)!!
            repo.updateDefaultAddress(
                DefaultAddressEntity(
                    newDefaultAddress.userId,
                    newDefaultAddress.addressId,
                    currentDefaultAddress.id
                )
            )
            _currentUserDefaultAddress.value = newDefaultAddress
        }
    }


    fun addUserAddress(address: Address) {
        viewModelScope.launch {
            repo.addUserAddress(address)
            if (_currentUserDefaultAddress.value == null) {
                val defaultAddress = DefaultAddressEntity(
                    address.userId,
                    address.addressId,
                    CodeGeneratorUtil.generateLastDefaultAddressId()
                )
                repo.addDefaultAddress(defaultAddress)
                _currentUserDefaultAddress.value = address
                chosenAddressPosition.value = 0
                chosenAddress.value = address
            }
            refreshUserAddresses()
        }
    }


    fun deleteUserAddress(address: Address) {
        viewModelScope.launch {
            repo.deleteUserAddress(address)
            refreshUserAddresses()
        }
    }

    fun updateUserAddress(address: Address) = viewModelScope.launch {
        repo.updateUserAddress(address)
        refreshUserAddresses()
    }

    fun refreshUserAddresses() {
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
            if (response == Response.ITEM_PRESENT_IN_CART) {
                for (i in items.cartItemEntity) {
                    if (i.productCode == productCode) {
                        if (i.quantity < 5) {
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
                Log.e(TAG,"NOT PRESENT, $cartItemId")

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
            val response = checkItemInCart(productCode)
            if (response == Response.ITEM_PRESENT_IN_CART) {
                Log.e(TAG, "Item in cart...")
                for (i in items.cartItemEntity) {
                    if (i.productCode == productCode) {
                        Log.e(TAG, "Product fount ${i.productCode}...")

                        if (i.quantity > 1) {
                            Log.e(TAG, "quantity > 1... decreased?")

                            myCartRepo.decreaseQuantity(i)
                            currentUserCart.value!!.totalItemsInCart--
                            _isRemovedFromCart.value = false
                            break
                        } else {
                            _isRemovedFromCart.value = true
                            myCartRepo.removeFromCart(i)


                        }
                    }
                }
            }
            refreshCart()
        }

    }

    fun updateChosenAddressPosition(addressId: String) {
        chosenAddress.value = repo.getAddress(addressId)
        val addresses = _currentUserAddresses.value!!.toMutableList()
        for (i in addresses.indices) {
            if (addresses[i].addressId == addressId) {
                chosenAddressPosition.value = i
                break
            }
        }
    }

    fun removeItemCompletely(productCode: Int) {
        viewModelScope.launch {
            for (i in _allCartItems.value!!) {
                if (i.productCode == productCode)
                    myCartRepo.removeFromCart(i)

                refreshCart()
            }

        }
    }


    fun emptyCart() {
        val cartId = currentUserCart.value!!.cartId

        viewModelScope.launch {
            myCartRepo.emptyCart(cartId)
            refreshCart()
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
            val wishListItemId = CodeGeneratorUtil.generateWishListItemId()
            myWishListRepo.addToWishList(
                WishListItemEntity(
                    productCode, wishListId, wishListItemId
                )
            )
            refreshWishList()
        }
    }

    fun removeFromWishListByProductCode(productCode: Int) {
        viewModelScope.launch {
            val wishListId = _currentUserWishList.value!!.wishListId
            var size = _allWishListItems.value?.size ?: 0
            if (size == 0)
                return@launch
            else {
                for (item in _allWishListItems.value!!) {
                    if (item.productCode == productCode) {
                        myWishListRepo.removeFromWishList(wishListId, item.id)
                        refreshWishList()
                        break
                    }
                }

            }
        }
    }

    fun removeProductFromWishList(wishListItemId: Int) {
        val wishListId = _currentUserWishList.value!!.wishListId
        viewModelScope.launch {
            myWishListRepo.removeFromWishList(wishListId, wishListItemId)
            refreshWishList()
        }
    }

    fun checkProductInWishList(productCode: Int): LiveData<Boolean> {
        val size = _allWishListItems.value?.size ?: 0
        if (size > 0) {
            for (i in _allWishListItems.value!!) {
                if (i.productCode == productCode)
                    return MutableLiveData(true)
            }
        }
        return MutableLiveData(false)
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

    fun deleteUserAccount() {
        viewModelScope.launch {
            repo.deleteUser(currentUser.value!!)
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



