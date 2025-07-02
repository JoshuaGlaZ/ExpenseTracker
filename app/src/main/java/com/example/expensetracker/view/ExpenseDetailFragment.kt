package com.example.expensetracker.view

import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.databinding.FragmentExpenseDetailBinding
import com.example.expensetracker.viewmodel.DetailViewModel
import java.util.Locale

class ExpenseDetailFragment : DialogFragment() {
    private lateinit var binding: FragmentExpenseDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenseDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expense_id = ExpenseDetailFragmentArgs.fromBundle(requireArguments()).expenseId
        Log.d("ExpenseDetail", "Expense ID: $expense_id")
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getDetailExpense(expense_id)

        viewModel.expenseD.observe(viewLifecycleOwner, Observer{
            if (it != null) {
                val displayFormat =
                    SimpleDateFormat("MMM dd, yyyy HH:mm a", Locale.getDefault())
                val formattedDate: String = displayFormat.format(it!!.timestamp)

                binding.txtDatetime.text = formattedDate
                binding.txtNote.text = it.note

                val formatted = NumberFormat.getNumberInstance(Locale("in", "ID"))
                    .format(it.nominal)
                binding.txtNominal.text = "IDR ${formatted}"
                viewModel.getDetailBudget(it.budget_id)
            }
        })

        viewModel.budgetD.observe(viewLifecycleOwner) { budget ->
            binding.chipNote.text = budget?.name ?: "Unknown Budget"
        }

        binding.btnAddBudget.setOnClickListener {
            dismiss()  // This will close the dialog
        }
    }
}