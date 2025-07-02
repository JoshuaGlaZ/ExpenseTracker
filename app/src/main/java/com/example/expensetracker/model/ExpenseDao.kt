package com.example.expensetracker.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
    @Query("SELECT * FROM user WHERE id = :id")
    fun selectUser(id: Int): User?
    @Query("SELECT * FROM user WHERE username = :username")
    fun selectUserByUsername(username: String): User?
    @Update
    fun updateUser(user: User): Int
    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    fun loginUser(username: String, password: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBudget(budget: Budget): Long
    @Query("SELECT * FROM budget WHERE user_id = :user_id ORDER BY name ASC")
    fun selectAllBudgets(user_id: Int): List<Budget>
    @Query("SELECT * FROM budget WHERE id = :id LIMIT 1")
    fun selectBudget(id: Int): Budget
    @Update
    fun updateBudget(budget: Budget): Int
    @Delete
    fun deleteBudget(budget: Budget): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: Expense): Long
    @Query("SELECT * FROM expense WHERE user_id = :userId ORDER BY timestamp DESC")
    fun selectAllExpenses(userId: Int): List<Expense>
    @Query("SELECT * FROM expense WHERE id = :id LIMIT 1")
    fun selectExpense(id: Int): Expense
    @Update
    fun updateExpense(expense: Expense): Int
    @Delete
    fun deleteExpense(expense: Expense): Int
    @Query("SELECT SUM(nominal) FROM expense WHERE user_id = :user_id AND budget_id = :budget_id")
    fun selectTotalNominal(user_id: Int, budget_id: Int): Double?

}
