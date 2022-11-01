package com.example.groceryshoppingapplication.data

import androidx.room.*
import com.example.groceryshoppingapplication.models.Address
import com.example.groceryshoppingapplication.models.DefaultAddressEntity
import com.example.groceryshoppingapplication.models.User
import com.example.groceryshoppingapplication.relations.*

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
    suspend fun addDefaultAddress(defaultAddressEntity: DefaultAddressEntity)

    @Update
    suspend fun updateDefaultAddress(defaultAddressEntity: DefaultAddressEntity)


    @Insert
    suspend fun addUserAddress(address: Address)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserAddress(address: Address)

    @Delete
    suspend fun deleteUserAddress(address: Address)

    @Query("select * from Address where addressId = :addressId")
    fun getAddress(addressId:String):Address

    @Query("select * from DefaultAddressEntity where userId = :userId")
    fun getUserDefaultAddressId(userId: String):DefaultAddressEntity?

    @Transaction
    @Query("select * from User where userId = :userId")
    fun getUserCartDetails(userId:String): UserAndCart

    @Transaction
    @Query("select * from User where userId = :userId")
    fun getUserWishList(userId: String): UserAndWishList


    @Transaction
    @Query("select * from OrderDetail order by orderDate desc")
    fun getUserOrderDetails(): UserAndOrders?


}
