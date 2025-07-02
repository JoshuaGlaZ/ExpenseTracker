package com.example.expensetracker.viewmodel

import android.app.Application
import android.icu.text.NumberFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetracker.model.ExpenseDatabase
import com.example.expensetracker.model.ReportItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ExpenseDatabase.getInstance(application).expenseDao()

    val reportLD = MutableLiveData<List<ReportItem>>()
    val totalSummary = MutableLiveData<String>()

    fun getReport(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val budgets = dao.selectAllBudgets(userId)
            val reports = budgets.map { budget ->
                val totalExpense = dao.selectTotalNominal(userId, budget.id) ?: 0.0
                ReportItem(
                    budgetName = budget.name,
                    budgetLimit = budget.amount,
                    expenseUsed = totalExpense
                )
            }

            val totalExpenses = reports.sumOf { it.expenseUsed }
            val totalBudget = reports.sumOf { it.budgetLimit }

            reportLD.postValue(reports)
            val formattedAmount = NumberFormat.getNumberInstance(Locale("in", "ID"))
            totalSummary.postValue("IDR ${formattedAmount.format(totalExpenses.toInt())} / " +
                    "IDR ${formattedAmount.format(totalBudget.toInt())}")
        }
    }
}
