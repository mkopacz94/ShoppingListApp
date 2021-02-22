package com.example.shoppinglistapp.Data.Repositories

import androidx.annotation.WorkerThread
import com.example.shoppinglistapp.Data.Daos.ItemUsedDao
import com.example.shoppinglistapp.Data.Entities.ItemUsed
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

class ItemsUsedRepository(private val itemUsedDao: ItemUsedDao) {

    val allUsedItems: Flow<List<ItemUsed>> = itemUsedDao.getAllUsedItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(item: ItemUsed) {
        itemUsedDao.insertItem(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(item: ItemUsed) {
        itemUsedDao.updateItem(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(item: ItemUsed) {
        itemUsedDao.deleteItem(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        itemUsedDao.deleteAll()
    }
}