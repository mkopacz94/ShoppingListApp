package com.example.shoppinglistapp.Data.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_used_table")
data class ItemUsed(@PrimaryKey(autoGenerate = true) val id: Int,
                    @ColumnInfo(name = "item") val item: String,
                    @ColumnInfo(name = "timesUsed") var timesUsed: Int) {
}