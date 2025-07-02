package com.example.expensetracker.view

import SessionManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentBudgetNewBinding
import com.example.expensetracker.model.Budget
import com.example.expensetracker.viewmodel.DetailViewModel
import com.example.expensetracker.viewmodel.ListViewModel

class BudgetNewFragment : Fragment() {
    private lateinit var binding: FragmentBudgetNewBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetNewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user_id = BudgetNewFragmentArgs.fromBundle(requireArguments()).userId

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

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

            viewModel.addNewBudget(Budget(amount=amount, name=name, user_id=user_id))
            Toast.makeText(view.context, "Budget ${name} added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
        }
    }
}