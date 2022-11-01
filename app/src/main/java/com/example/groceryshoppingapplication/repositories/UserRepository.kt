package com.example.groceryshoppingapplication.repositories

import androidx.lifecycle.MutableLiveData
import com.example.groceryshoppingapplication.data.AppDatabase
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.DefaultAddressEntity
import com.example.groceryshoppingapplication.models.User

class UserRepository(database: AppDatabase) {
    private val userDao = database.getUserDao()

    private var _currentUser= MutableLiveData<User>()
    val currentUser = _currentUser


    fun loginUser(mobileNumber: String) = userDao.getUserAccount(mobileNumber)




   // val allUsers = database.getUserDao().
    suspend fun createUser(user: User) = userDao.addUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)



    fun getUserAddresses(userId:String) = userDao.getUserAddresses(userId)

    suspend fun addUserAddress(address: Address) = userDao.addUserAddress(address)

    suspend fun updateUserAddress(address: Address) = userDao.updateUserAddress(address)

    suspend fun deleteUserAddress(address: Address) = userDao.deleteUserAddress(address)

    suspend fun addDefaultAddress(defaultAddressEntity: DefaultAddressEntity) = userDao.addDefaultAddress(defaultAddressEntity)

    suspend fun updateDefaultAddress(defaultAddressEntity: DefaultAddressEntity) = userDao.updateDefaultAddress(defaultAddressEntity)


    fun getAddress(addressId:String) = userDao.getAddress(addressId)

    fun getDefaultAddressId(userId: String) = userDao.getUserDefaultAddressId(userId)

    fun getUserCartDetails(userId:String) = userDao.getUserCartDetails(userId)

    fun getUserWishListDetails(userId: String) = userDao.getUserWishList(userId)

    fun getUserOrderDetails(userId: String) = userDao.getUserOrderDetails()
}