package com.example.groceryshoppingapplication.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.data.UserDAO
import com.example.groceryshoppingapplication.models.User

class UserRepository(database: AppDatabase) {
    private val userDao = database.getUserDao()

    private var _currentUser= MutableLiveData<User>()
    val currentUser = _currentUser


    fun loginUser(mobileNumber: String): LiveData<User>? {
        return userDao.getUserAccount(mobileNumber)

    }


   // val allUsers = database.getUserDao().
    suspend fun createUser(user: User) = userDao.addUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    fun getUserAccount(mobileNumber:String) = userDao.getUserAccount(mobileNumber)

    fun getUserCartDetails(userId:String) = userDao.getUserCartDetails(userId)

    fun getUserOrderDetails(userId: String) = userDao.getUserOrderDetails(userId)
}