package com.example.expensetracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.expensetracker.databinding.FragmentExpenseTrackerBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.viewmodel.ListViewModel

class ExpenseTrackerFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    private val expenseListAdapter  = ExpenseListAdapter(arrayListOf())
//    private val expenseListAdapter  = ExpenseListAdapter(arrayListOf(), { item -> viewModel.clearTask(item) })
    private lateinit var binding: FragmentExpenseTrackerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseTrackerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user_id = SessionManager.getUserId()

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.getExpenses(user_id)

        viewModel.expensesLD.observe(viewLifecycleOwner, Observer {
            expenseListAdapter.updateExpenseList(it)
            binding.swipeRefreshExpense.isRefreshing = false
        })

        binding.recViewExpense.layoutManager = LinearLayoutManager(context)
        binding.recViewExpense.adapter = expenseListAdapter
        binding.fabExpense.setOnClickListener {
            val action = ExpenseTrackerFragmentDirections.actionExpenseNewFragment(user_id)
            Navigation.findNavController(it).navigate(action)
        }
        binding.swipeRefreshExpense.setOnRefreshListener {
            viewModel.getExpenses(user_id)
        }
    }
}