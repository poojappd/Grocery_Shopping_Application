package com.example.groceryshoppingapplication.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.repositories.CartRepository
import com.example.groceryshoppingapplication.repositories.OrdersRepository
import com.example.groceryshoppingapplication.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(applicationContext: Context) : ViewModel() {

    private val repo = UserRepository(AppDatabase.getDatabase(applicationContext))
    private val myOrdersRepo = OrdersRepository(AppDatabase.getDatabase(applicationContext))
    private val myCartRepo = CartRepository(AppDatabase.getDatabase(applicationContext))
    private var _currentUser = MutableLiveData<User>()
    private var _currentUserCart = MutableLiveData<CartEntity>()
    private var _currentUserOrders = MutableLiveData<List<OrderDetail>>()
    private var lastId = MutableLiveData<Int?>()
    private val _isRemovedFromCart = MutableLiveData<Boolean>()
    private val _allCartItems = MutableLiveData<List<CartItemEntity>>()

    val isRemovedFromCart: LiveData<Boolean>
        get() = _isRemovedFromCart
    val allCartItems: LiveData<List<CartItemEntity>>
        get() = _allCartItems


    val currentUser = _currentUser
    val currentUserCart = _currentUserCart
    val currentUserOrders = _currentUserOrders

    fun loginUser(mobileNumber: String): Response {
        val user = repo.loginUser(mobileNumber)
        user?.let {
            _currentUser.value = it
            _currentUserCart.value = repo.getUserCartDetails(it.userId).cartEntity
            _currentUserOrders.value = repo.getUserOrderDetails(it.userId)?.ordersInfo
            viewModelScope.launch {
            updateCartItemsFromCart()
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

    suspend fun updateCartItemsFromCart(){
            _allCartItems.value = myCartRepo.getCartItemsFromCart(currentUserCart.value!!.cartId).cartItemEntity
    }

    private suspend fun updateLastId() {

        lastId.value = myCartRepo.getLastCartItemId(currentUserCart.value!!.cartId)
        Log.e(TAG, "updateion - " + lastId.value.toString())

    }

    fun addToCart(productCode: Int) {

        viewModelScope.launch {
            val items = myCartRepo.getCartItemsFromCart(currentUserCart.value!!.cartId)
            val response = checkItemInCart(productCode)
            Log.e(TAG, response.message)
            if (response == Response.ITEM_PRESENT_IN_CART) {
                for (i in items.cartItemEntity) {
                    if (i.productCode == productCode) {
                        myCartRepo.increaseQuantity(i)
                        currentUserCart.value!!.totalItemsInCart++
                        break
                    }
                }
            } else {
                updateLastId()
                val value = lastId.value?.inc() ?: 1
                myCartRepo.addToCart(
                    CartItemEntity(
                        productCode,
                        currentUserCart.value!!.cartId,
                        1,
                        value
                    )
                )
            }
            updateCartItemsFromCart()
        }

    }

    fun removeFromCart(productCode: Int) {
        viewModelScope.launch {
            val items = myCartRepo.getCartItemsFromCart(currentUserCart.value!!.cartId)
           items.cartItemEntity.forEach {
               Log.e(TAG,"-----------------------------"+ "now in cart: ->"+ it.toString()) }

            val response = checkItemInCart(productCode)
            Log.e(TAG, response.message)
            if (response == Response.ITEM_PRESENT_IN_CART) {
                for (i in items.cartItemEntity) {
                    if (i.productCode == productCode) {
                        if (i.quantity > 1) {
                            myCartRepo.decreaseQuantity(i)
                            Log.e(TAG,"***********"+ "decreased ->"+ i.productCode.toString())

                        currentUserCart.value!!.totalItemsInCart--
                            _isRemovedFromCart.value = false
                            break
                        } else {
                            _isRemovedFromCart.value = true
                            myCartRepo.removeFromCart(
                                CartItemEntity(
                                    productCode,
                                    currentUserCart.value!!.cartId
                                )

                            )
                        Log.e(TAG,"***********"+ "removed ->"+ i.productCode.toString())


                }
                    }
                }
            }
            updateCartItemsFromCart()
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

    fun createUser(user: User, cart: CartEntity) {
        viewModelScope.launch {
            repo.createUser(user)
            myCartRepo.createCartForNewUser(cart)
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


