package com.example.expensetracker.view

import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentExpenseNewBinding
import com.example.expensetracker.model.Expense
import com.example.expensetracker.viewmodel.DetailViewModel
import com.example.expensetracker.viewmodel.ListViewModel
import java.util.Locale


class ExpenseNewFragment : Fragment() {
    private lateinit var binding: FragmentExpenseNewBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var listViewModel: ListViewModel
    private var user_id: Int = -1
    private var budgetMap: Map<String, Int> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user_id = ExpenseNewFragmentArgs.fromBundle(requireArguments()).userId

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.getBudgets(user_id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenseNewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel.budgetLD.observe(viewLifecycleOwner){ budgets ->
            budgetMap = budgets.associate { it.name to it.id }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                budgets.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBudget.adapter = adapter
        }

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        viewModel.budgetAmount.observe(viewLifecycleOwner) { total ->
            val expenses = viewModel.totalExpenses.value ?: 0.0
            updateProgress(total, expenses)
            val formatted = NumberFormat.getNumberInstance(Locale("in", "ID"))
                .format(total)
            binding.txtTotalBudget.text = "IDR $formatted"
        }

        viewModel.totalExpenses.observe(viewLifecycleOwner) { expenses ->
            val total = viewModel.budgetAmount.value ?: 0.0
            updateProgress(total, expenses)
            val formatted = NumberFormat.getNumberInstance(Locale("in", "ID"))
                .format(expenses)
            binding.txtBudgetLeft.text = "IDR $formatted"
        }

        val displayFormat =
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentTime = System.currentTimeMillis()
        val formattedDate: String = displayFormat.format(currentTime)

        binding.txtDate.text = formattedDate
        binding.btnAddBudget.setOnClickListener {
            val nominal: Double? = binding.txtNominal.text.toString().toDoubleOrNull()
            val notes: String = binding.txtNotes.text.toString()
            val selectedName = binding.spinnerBudget.selectedItem as? String
            val selectedBudgetId = budgetMap[selectedName] ?: -1

            if (selectedBudgetId == -1) {
                Toast.makeText(activity, "Please select a budget", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtNotes.text.toString().isEmpty() || notes.isEmpty()) {
                Toast.makeText(activity, "All fields must be filled", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (nominal == null) {
                Toast.makeText(activity, "Please enter a valid nominal", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (nominal <= 0) {
                Toast.makeText(activity, "Amount can't be negative", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val totalBudget = viewModel.budgetAmount.value ?: 0.0
            val totalUsed = viewModel.totalExpenses.value ?: 0.0
            val remaining = totalBudget - totalUsed

            if (nominal > remaining) {
                Toast.makeText(activity, "Nominal exceeds budget remaining (IDR $remaining)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addNewExpense(Expense(
                nominal = nominal, note = notes, timestamp = System.currentTimeMillis(),
                user_id = user_id, budget_id = selectedBudgetId
            ))
            Toast.makeText(view.context, "Expense added for ${selectedName}", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()

        }

        binding.spinnerBudget.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedName = parent.getItemAtPosition(position) as String
                val selectedBudgetId = budgetMap[selectedName] ?: return

                viewModel.getExpenseWithBudget(user_id, selectedBudgetId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun updateProgress(total: Double, expenses: Double) {
        if (total == 0.0) {
            binding.progressBar.progress = 0
            return
        }
        val progress = (expenses / total * 100).toInt().coerceIn(0, 100)
        binding.progressBar.progress = progress
    }
}