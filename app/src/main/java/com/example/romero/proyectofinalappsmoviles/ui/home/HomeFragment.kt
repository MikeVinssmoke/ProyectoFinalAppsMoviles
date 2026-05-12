package com.example.romero.proyectofinalappsmoviles.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentHomeBinding
import com.example.romero.proyectofinalappsmoviles.util.NetworkUtils
import com.example.romero.proyectofinalappsmoviles.viewmodel.ReportViewModel
import com.example.romero.proyectofinalappsmoviles.viewmodel.SyncViewModel

class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!
    private val reportVm: ReportViewModel by viewModels()
    private val syncVm: SyncViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentHomeBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reportVm.totalCount.observe(viewLifecycleOwner)   { b.tvTotal.text   = it.toString() }
        reportVm.pendingCount.observe(viewLifecycleOwner) { b.tvPending.text = it.toString() }
        reportVm.syncedCount.observe(viewLifecycleOwner)  { b.tvSynced.text  = it.toString() }

        syncVm.pendingCount.observe(viewLifecycleOwner) { count ->
            b.tvSyncStatus.text = if (count > 0) "$count cambios pendientes" else "Todo sincronizado"
        }

        b.tvOnlineStatus.text =
            if (NetworkUtils.isOnline(requireContext())) "🟢 Online" else "🔴 Offline"

        b.btnNewReport.setOnClickListener  { findNavController().navigate(R.id.action_home_to_createReport) }
        b.btnViewReports.setOnClickListener{ findNavController().navigate(R.id.action_home_to_reportList) }
        b.btnSync.setOnClickListener       { findNavController().navigate(R.id.action_home_to_sync) }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}