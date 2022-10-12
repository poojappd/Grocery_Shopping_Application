package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.relations.UserAndAddresses
import com.example.groceryshoppingapplication.relations.UserAndCart
import com.example.groceryshoppingapplication.relations.UserAndOrders

@Dao
interface UserDAO{

    @Insert
    suspend fun addUser(user:User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from User where mobileNumber = :mobileNumber")
    fun getUserAccount(mobileNumber:String):User?

    @Transaction
    @Query("select * from User where userId = :userId")
    fun getUserAddresses(userId:String): UserAndAddresses

    @Insert
    suspend fun addUserAddress(address: Address)

    @Update
    suspend fun updateUserAddress(address: Address)

    @Delete
    suspend fun deleteUserAddress(address: Address)

    @Transaction
    @Query("select * from User where userId = :userId")
    fun getUserCartDetails(userId:String): UserAndCart

    @Transaction
    @Query("select * from OrderDetail")
    fun getUserOrderDetails(): UserAndOrders?

}
