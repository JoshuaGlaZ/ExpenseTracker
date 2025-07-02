package com.example.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    @ColumnInfo(name="nominal")
    var nominal:Double,
    @ColumnInfo(name="timestamp")
    var timestamp:Long,
    @ColumnInfo(name="note")
    var note:String,
    @ColumnInfo(name="user_id")
    var user_id:Int,
    @ColumnInfo(name="budget_id")
    var budget_id:Int,
)