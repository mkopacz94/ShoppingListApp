package com.example.shoppinglistapp.Application

import android.app.Application
import com.example.shoppinglistapp.Data.Database.ShoppingItemsDatabase
import com.example.shoppinglistapp.Data.Repositories.ShoppingItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ShoppingListApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ShoppingItemsDatabase.getDatabase(this) }
    val repository by lazy { ShoppingItemsRepository(database.itemsDao()) }
}