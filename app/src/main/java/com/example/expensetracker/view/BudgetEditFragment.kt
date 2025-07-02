package com.example.expensetracker.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentBudgetNewBinding
import com.example.expensetracker.model.Budget
import com.example.expensetracker.viewmodel.DetailViewModel
import com.example.expensetracker.viewmodel.ListViewModel

class BudgetEditFragment : Fragment() {
    private lateinit var binding: FragmentBudgetNewBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetNewBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val budget_id = BudgetEditFragmentArgs.fromBundle(requireArguments()).budgetId
        val user_id = BudgetEditFragmentArgs.fromBundle(requireArguments()).userId

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getDetailBudget(budget_id)

        viewModel.budgetD.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.txtName.setText(it.name)
                binding.txtAmount.setText(it.amount.toString())
            }
        })

        viewModel.updateBudgetStatus.observe(viewLifecycleOwner) { status ->
            if (status) {
                Toast.makeText(view.context, "Budget successfully updated", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(activity, "Amount can't be less than total expenses", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.txtHead.text = "Edit Budget"
        binding.btnAddBudget.text = "Save Budget"
        binding.btnAddBudget.setOnClickListener {
            val amount: Double? = binding.txtAmount.text.toString().toDoubleOrNull()
            val name: String = binding.txtName.text.toString()

            if (binding.txtAmount.text.toString().isEmpty() || name.isEmpty()) {
                Toast.makeText(activity, "All fields must be filled", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (amount == null) {
                Toast.makeText(activity, "Please enter a valid amount", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (amount <= 0) {
                Toast.makeText(activity, "Amount can't be negative", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            viewModel.updateBudget(Budget(id = budget_id, amount=amount, name=name, user_id=user_id))
        }
    }
}