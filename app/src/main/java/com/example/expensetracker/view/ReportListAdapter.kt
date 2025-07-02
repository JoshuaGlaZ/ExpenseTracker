package com.example.expensetracker.view

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ExpenseListItemBinding
import com.example.expensetracker.databinding.ReportListItemBinding
import com.example.expensetracker.model.ReportItem
import java.util.Locale

class ReportListAdapter(val reportList: ArrayList<ReportItem>)
    : RecyclerView.Adapter<ReportListAdapter.ReportViewHolder>() {

    class ReportViewHolder(var binding: ReportListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ReportListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }


    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]
        val formattedAmount = NumberFormat.getNumberInstance(Locale("in", "ID"))

        val progressPercent = ((report.expenseUsed / report.budgetLimit) * 100).toInt().coerceIn(0, 100)
        val budgetLeft = report.budgetLimit - report.expenseUsed

        holder.binding.txtBudgetName.text = report.budgetName
        holder.binding.txtBudgetUsed.text = "IDR ${formattedAmount.format(report.expenseUsed)}"
        holder.binding.txtMaxBudget.text = "IDR ${formattedAmount.format(report.budgetLimit)}"
        holder.binding.progressBudget.progress = progressPercent
        holder.binding.txtBudgetLeftReport.text = "IDR ${formattedAmount.format(budgetLeft)}"
    }

    fun updateReportList(newList: List<ReportItem>) {
        reportList.clear()
        reportList.addAll(newList)
        notifyDataSetChanged()
    }
}