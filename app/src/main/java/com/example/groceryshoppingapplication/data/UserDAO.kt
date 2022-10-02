package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.models.User
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
    fun getUserCartDetails(userId:String): UserAndCart

    @Transaction
    @Query("select * from OrderDetail")
    fun getUserOrderDetails(): UserAndOrders?

}
