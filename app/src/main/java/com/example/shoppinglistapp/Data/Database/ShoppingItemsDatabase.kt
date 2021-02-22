package com.example.shoppinglistapp.Data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglistapp.Data.Daos.ItemUsedDao
import com.example.shoppinglistapp.Data.Daos.ShoppingItemDao
import com.example.shoppinglistapp.Data.Entities.ItemUsed
import com.example.shoppinglistapp.Data.Entities.ShoppingItem

@Database(entities = [ShoppingItem::class, ItemUsed::class], version = 3, exportSchema = false)
abstract class ShoppingItemsDatabase : RoomDatabase() {

    abstract fun itemDao(): ShoppingItemDao
    abstract fun itemUsedDao(): ItemUsedDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ShoppingItemsDatabase? = null

        fun getDatabase(context: Context): ShoppingItemsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingItemsDatabase::class.java,
                    "items_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}