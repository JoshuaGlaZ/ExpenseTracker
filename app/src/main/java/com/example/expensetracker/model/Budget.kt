package com.example.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    @ColumnInfo(name="amount")
    var amount:Double,
    @ColumnInfo(name="name")
    var name:String,
    @ColumnInfo(name="user_id")
    var user_id:Int,
)
