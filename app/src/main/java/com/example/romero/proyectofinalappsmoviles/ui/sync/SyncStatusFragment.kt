package com.example.romero.proyectofinalappsmoviles.ui.sync

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentSyncStatusBinding
import com.example.romero.proyectofinalappsmoviles.viewmodel.SyncViewModel

class SyncStatusFragment : Fragment() {
    private var _b: FragmentSyncStatusBinding? = null
    private val b get() = _b!!
    private val vm: SyncViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSyncStatusBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.checkConnectivity()

        vm.isOnline.observe(viewLifecycleOwner) { online ->
            b.tvConnectionStatus.text = if (online) "🟢 Conectado" else "🔴 Sin conexión"
            b.btnSync.isEnabled = online
        }

        vm.pendingCount.observe(viewLifecycleOwner) { count ->
            b.tvPendingCount.text = "$count cambios en cola"
        }

        vm.isSyncing.observe(viewLifecycleOwner) { syncing ->
            b.progressSync.visibility = if (syncing) View.VISIBLE else View.GONE
            b.btnSync.text = if (syncing) "Sincronizando…" else "Sincronizar ahora"
        }

        vm.syncResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            val msg = if (result.isSuccess) "✓ Sincronización completada" else "✗ Error al sincronizar"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        b.btnSync.setOnClickListener    { vm.synchronize() }
        b.btnRefresh.setOnClickListener { vm.checkConnectivity() }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}