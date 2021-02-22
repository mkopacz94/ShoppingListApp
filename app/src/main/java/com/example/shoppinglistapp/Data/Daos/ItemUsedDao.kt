package com.example.shoppinglistapp.Data.Daos

import androidx.room.*
import com.example.shoppinglistapp.Data.Entities.ItemUsed
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemUsedDao {
    @Query("SELECT * FROM items_used_table")
    fun getAllUsedItems(): Flow<List<ItemUsed>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: ItemUsed)

    @Update
    suspend fun updateItem(item: ItemUsed)

    @Delete
    suspend fun deleteItem(item: ItemUsed)

    @Query("DELETE FROM items_used_table")
    suspend fun deleteAll()
}