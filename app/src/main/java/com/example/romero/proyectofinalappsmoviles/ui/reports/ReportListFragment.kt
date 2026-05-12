package com.example.romero.proyectofinalappsmoviles.ui.reports

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.adapter.ReportAdapter
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentReportListBinding
import com.example.romero.proyectofinalappsmoviles.viewmodel.ReportViewModel

class ReportListFragment : Fragment() {
    private var _b: FragmentReportListBinding? = null
    private val b get() = _b!!
    private val vm: ReportViewModel by viewModels()
    private lateinit var adapter: ReportAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentReportListBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ReportAdapter { report ->
            findNavController().navigate(
                R.id.action_reportList_to_reportDetail,
                bundleOf("reportId" to report.id)
            )
        }
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter

        vm.filteredReports.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            b.emptyView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        b.chipAll.setOnClickListener      { vm.setStatusFilter(null) }
        b.chipOpen.setOnClickListener     { vm.setStatusFilter("ABIERTO") }
        b.chipProgress.setOnClickListener { vm.setStatusFilter("EN_PROCESO") }
        b.chipClosed.setOnClickListener   { vm.setStatusFilter("CERRADO") }

        b.fabCreate.setOnClickListener { findNavController().navigate(R.id.action_reportList_to_createReport) }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}