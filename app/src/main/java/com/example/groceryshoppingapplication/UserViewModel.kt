package com.example.groceryshoppingapplication

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.enums.Response
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.OrderDetail
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(applicationContext: Context) : ViewModel() {

    private val repo = UserRepository(AppDatabase.getDatabase(applicationContext))
    private val ordersRepo =
    private var _currentUser = MutableLiveData<User>()
    private var _currentUserCart = MutableLiveData<CartEntity>()
    private var _currentUserOrders = MutableLiveData<List<OrderDetail>>()

    val currentUser = _currentUser
    val currentUserCart = _currentUserCart
    val currentUserOrders = _currentUserOrders

    fun loginUser(mobileNumber: String): Response {
        val user = repo.loginUser(mobileNumber)
        user?.value?.let {
            _currentUser.value = it
            _currentUserCart.value = repo.getUserCartDetails(it.userId).cartEntity
            _currentUserOrders.value = repo.getUserOrderDetails(it.userId).ordersInfo
            return Response.LOGGED_IN_SUCCESSFULLY
        }
        return Response.NO_SUCH_USER
    }


}