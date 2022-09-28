package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.relations.UserAndCart
import com.example.groceryshoppingapplication.relations.UserAndOrders

@Dao
interface userDAO{

    @Insert
    suspend fun addUser(user:User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from User where mobileNumber = :mobileNumber")
    suspend fun getUserAccount(mobileNumber:String)

    @Query("select * from User")
    suspend fun getUserCartId(userId:String):UserAndCart

    @Query("select * from OrderDetail")
    suspend fun getAllOrderDetails(userId: String): UserAndOrders

}
