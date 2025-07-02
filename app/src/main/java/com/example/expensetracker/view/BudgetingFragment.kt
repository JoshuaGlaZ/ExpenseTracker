package com.example.expensetracker.view

import SessionManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentBudgetingBinding
import com.example.expensetracker.viewmodel.ListViewModel

class BudgetingFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    private val budgetListAdapter  = BudgetListAdapter(arrayListOf())
    private lateinit var binding: FragmentBudgetingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user_id = SessionManager.getUserId()

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.getBudgets(user_id)

        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            budgetListAdapter.updateBudgetList(it)
            binding.swipeRefreshBudget.isRefreshing = false
        })

        binding.recViewBudget.layoutManager = LinearLayoutManager(context)
        binding.recViewBudget.adapter = budgetListAdapter
        binding.swipeRefreshBudget.setOnRefreshListener {
            viewModel.getBudgets(user_id)
        }

        binding.fabBudget.setOnClickListener {
            val action = BudgetingFragmentDirections.actionNewBudgetFragment(user_id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}