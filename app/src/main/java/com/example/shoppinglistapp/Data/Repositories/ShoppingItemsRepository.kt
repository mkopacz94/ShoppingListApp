package com.example.shoppinglistapp.Data.Repositories

import androidx.annotation.WorkerThread
import com.example.shoppinglistapp.Data.Daos.ShoppingItemDao
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

class ShoppingItemsRepository(private val shoppingItemDao: ShoppingItemDao) {

    val allItems: Flow<List<ShoppingItem>> = shoppingItemDao.getAllItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(item: ShoppingItem) {
        shoppingItemDao.insertItem(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(item: ShoppingItem) {
        shoppingItemDao.updateItem(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.deleteItem(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        shoppingItemDao.deleteAll()
    }
}