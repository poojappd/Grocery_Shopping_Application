package com.example.groceryshoppingapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.groceryshoppingapplication.TypeConverters.*
import com.example.groceryshoppingapplication.models.CartEntity
import com.example.groceryshoppingapplication.models.CartItemEntity
import com.example.groceryshoppingapplication.models.GroceryItemEntity
import com.example.groceryshoppingapplication.models.demoUser


@Database(entities = arrayOf(GroceryItemEntity::class, CartEntity::class, CartItemEntity::class,demoUser::class), version = 4, exportSchema = true)
@TypeConverters(
    GeneralCategoryConverter::class,
    SubCategoryConverter::class,
    PackagingConverter::class,
    MeasuringUnitConverter::class,
    ProductAvailabilityConverter::class
        )
abstract class AppDatabase : RoomDatabase(){
    abstract fun getInventoryDao(): InventoryDAO
    abstract fun getCartDao(): CartDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            if(INSTANCE != null){
                return INSTANCE as AppDatabase
            }

            else synchronized(this) {

                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "grocery_db"
                ).allowMainThreadQueries()
                    .createFromAsset("database/grocery_db.db")
                    .build()
                INSTANCE = instance
                val sdb = INSTANCE!!.openHelper.writableDatabase

                return instance
            }
            }
        }
    }


