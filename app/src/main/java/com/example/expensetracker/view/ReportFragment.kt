package com.example.expensetracker.view

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentReportBinding
import com.example.expensetracker.viewmodel.ReportViewModel
import java.util.Locale

class ReportFragment : Fragment() {
    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: ReportViewModel
    private val reportListAdapter = ReportListAdapter(arrayListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        val userId = SessionManager.getUserId()

        viewModel.getReport(userId)
        viewModel.reportLD.observe(viewLifecycleOwner) { reportList ->
            reportListAdapter.updateReportList(reportList)
        }

        val formattedAmount = NumberFormat.getNumberInstance(Locale("in", "ID"))

        viewModel.totalSummary.observe(viewLifecycleOwner) { summary ->
            binding.txtExpenseBudget.text = summary
        }

        binding.recViewReport.layoutManager = LinearLayoutManager(context)
        binding.recViewReport.adapter = reportListAdapter
    }
}