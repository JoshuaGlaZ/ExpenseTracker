package com.example.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    @ColumnInfo(name="username")
    val username: String,
    @ColumnInfo(name="password")
    var password: String,
    @ColumnInfo(name="firstname")
    val firstname: String,
    @ColumnInfo(name="lastname")
    val lastname: String,
)