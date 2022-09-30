package com.example.groceryshoppingapplication.data

import androidx.lifecycle.LiveData
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
    fun getUserAccount(mobileNumber:String):LiveData<User>?

    @Query("select * from User")
    fun getUserCartDetails(userId:String):UserAndCart

    @Query("select * from OrderDetail")
    fun getUserOrderDetails(userId: String): UserAndOrders

}
