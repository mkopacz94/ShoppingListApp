package com.example.shoppinglistapp.Data.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class ShoppingItem(@PrimaryKey(autoGenerate = true) val id: Int,
                        @ColumnInfo(name = "item") var item: String,
                        @ColumnInfo(name = "bought") var bought: Boolean) {
}