package com.example.expensetracker.view

import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.BudgetListItemBinding
import com.example.expensetracker.model.Budget
import java.util.Locale



class BudgetListAdapter(val budgetList:ArrayList<Budget>)
    : RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>()
{
    class BudgetViewHolder(var binding: BudgetListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = BudgetListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.binding.txtName.text = budgetList[position].name
        holder.binding.txtName.setOnClickListener {
            val action = BudgetingFragmentDirections.actionBudgetEditFragment(
                budgetId = budgetList[position].id, userId = budgetList[position].user_id)
            Navigation.findNavController(it).navigate(action)
        }
        val formatted = NumberFormat.getNumberInstance(Locale("in", "ID"))
            .format(budgetList[position].amount)

        holder.binding.txtAmount.text = "IDR $formatted"
    }

    fun updateBudgetList(newBudgetList: ArrayList<Budget>) {
        budgetList.clear()
        budgetList.addAll(newBudgetList)
        notifyDataSetChanged()
    }
}