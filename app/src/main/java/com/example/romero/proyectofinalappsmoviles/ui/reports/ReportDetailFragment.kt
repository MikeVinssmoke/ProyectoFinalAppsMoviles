package com.example.romero.proyectofinalappsmoviles.ui.reports

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentReportDetailBinding
import com.example.romero.proyectofinalappsmoviles.util.DateUtils
import com.example.romero.proyectofinalappsmoviles.viewmodel.ReminderViewModel
import com.example.romero.proyectofinalappsmoviles.viewmodel.ReportViewModel
import java.util.Calendar

class ReportDetailFragment : Fragment() {
    private var _b: FragmentReportDetailBinding? = null
    private val b get() = _b!!
    private val vm: ReportViewModel by viewModels()
    private val reminderVm: ReminderViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentReportDetailBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val reportId = arguments?.getLong("reportId") ?: return
        vm.loadReport(reportId)

        vm.currentReport.observe(viewLifecycleOwner) { r ->
            if (r == null) return@observe
            b.tvTitle.text       = r.title
            b.tvDescription.text = r.description
            b.tvCategory.text    = "Categoría: ${r.category}"
            b.tvPriority.text    = "Prioridad: ${r.priority}"
            b.tvLocation.text    = "Ubicación: ${r.location}"
            b.tvDate.text        = "Fecha: ${DateUtils.formatDate(r.date)}"
            b.tvIconLetter.text  = r.category.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

            val (bg, label) = when (r.status) {
                "ABIERTO"    -> Pair(R.drawable.bg_status_open,     "Abierto")
                "EN_PROCESO" -> Pair(R.drawable.bg_status_progress, "En proceso")
                else         -> Pair(R.drawable.bg_status_closed,   "Cerrado")
            }
            b.tvStatus.setBackgroundResource(bg)
            b.tvStatus.text  = label
            b.tvSyncBadge.text = if (r.isSynced) "✓ Sincronizado" else "⏳ Pendiente sync"

            b.btnUpdateStatus.setOnClickListener {
                val estados = arrayOf("ABIERTO", "EN_PROCESO", "CERRADO")
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cambiar estado")
                    .setItems(estados) { _, which ->
                        val nuevoEstado = estados[which]
                        vm.updateReport(r.copy(status = nuevoEstado, isSynced = false))
                        Toast.makeText(requireContext(), "Estado → $nuevoEstado", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }

            b.btnEdit.setOnClickListener {
                findNavController().navigate(
                    R.id.action_reportDetail_to_editReport,
                    bundleOf("reportId" to r.id)
                )
            }
        }

        b.btnReminder.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                TimePickerDialog(requireContext(), { _, h, min ->
                    cal.set(y, m, d, h, min, 0)
                    reminderVm.scheduleReminder(reportId, cal.timeInMillis,
                        "Seguimiento: ${b.tvTitle.text}")
                    Toast.makeText(requireContext(), "Recordatorio programado", Toast.LENGTH_SHORT).show()
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}