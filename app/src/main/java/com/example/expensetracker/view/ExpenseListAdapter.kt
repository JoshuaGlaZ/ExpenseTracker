package com.example.expensetracker.view

import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ExpenseListItemBinding
import com.example.expensetracker.model.Expense
import java.util.Locale



class ExpenseListAdapter(val expenseList:ArrayList<Expense>)
    : RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>()
{
    class ExpenseViewHolder(var binding: ExpenseListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val displayFormat =
            SimpleDateFormat("MMM dd, yyyy HH:mm a", Locale.getDefault())
        val formattedDate: String = displayFormat.format(expenseList[position].timestamp)

        holder.binding.txtTimestamp.text = formattedDate
        holder.binding.chipNote.text = expenseList[position].note
        val formatted = NumberFormat.getNumberInstance(Locale("in", "ID"))
            .format(expenseList[position].nominal)
        holder.binding.txtNominal.text = "IDR ${formatted}"
        holder.binding.txtNominal.setOnClickListener {
            val action = ExpenseTrackerFragmentDirections.actionExpenseDetailFragment(expenseList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun updateExpenseList(newExpenseList: ArrayList<Expense>) {
        expenseList.clear()
        expenseList.addAll(newExpenseList)
        notifyDataSetChanged()
    }
}