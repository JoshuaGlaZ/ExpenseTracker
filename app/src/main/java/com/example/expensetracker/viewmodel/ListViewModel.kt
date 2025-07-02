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

class ListViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    val expensesLD = MutableLiveData<ArrayList<Expense>>()
    val budgetLD = MutableLiveData<ArrayList<Budget>>()

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val dao = ExpenseDatabase.getInstance(getApplication()).expenseDao()

    fun getBudgets(user_id: Int) {
        launch {
            val result = dao.selectAllBudgets(user_id)
            budgetLD.postValue(ArrayList(result))
        }
    }

    fun getExpenses(user_id: Int) {
        launch {
            val result = dao.selectAllExpenses(user_id)
            expensesLD.postValue(ArrayList(result))
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}


