package com.example.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.expensetracker.model.Budget
import com.example.expensetracker.model.Expense
import com.example.expensetracker.model.ExpenseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class DetailViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    val budgetD = MutableLiveData<Budget?>()
    val expenseD = MutableLiveData<Expense?>()
    val updateBudgetStatus = MutableLiveData<Boolean>()

    val budgetAmount = MutableLiveData<Double>()
    val totalExpenses = MutableLiveData<Double>()


    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val dao = ExpenseDatabase.getInstance(getApplication()).expenseDao()

    fun getDetailBudget(id: Int) {
        launch {
            val budget = dao.selectBudget(id)
            budgetD.postValue(budget)
        }
    }

    fun addNewBudget(budget: Budget) {
        launch {
            dao.insertBudget(budget)
        }
    }

    fun updateBudget(budget: Budget) {
        launch {
            val totalExpense = dao.selectTotalNominal(budget.user_id, budget.id) ?: 0.0
            if (budget.amount < totalExpense) {
                updateBudgetStatus.postValue(false)
            } else {
                dao.updateBudget(budget)
                updateBudgetStatus.postValue(true)
            }
        }
    }

    fun getExpenseWithBudget(user_id: Int, budget_id: Int) {
        launch {
            val budget = dao.selectBudget(budget_id)
            val expensesUsed = dao.selectTotalNominal(user_id, budget_id) ?: 0.0

            budgetAmount.postValue(budget?.amount ?: 0.0)
            totalExpenses.postValue(expensesUsed)
        }
    }

    fun getDetailExpense(id: Int) {
        launch {
            val expense = dao.selectExpense(id)
            expenseD.postValue(expense)
        }
    }

    fun addNewExpense(expense: Expense) {
        launch {
            dao.insertExpense(expense)
        }
    }
}