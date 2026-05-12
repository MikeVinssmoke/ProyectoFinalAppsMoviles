package com.example.romero.proyectofinalappsmoviles.ui.reports

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentEditReportBinding
import com.example.romero.proyectofinalappsmoviles.viewmodel.ReportViewModel

class EditReportFragment : Fragment() {
    private var _b: FragmentEditReportBinding? = null
    private val b get() = _b!!
    private val vm: ReportViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentEditReportBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val reportId = arguments?.getLong("reportId") ?: return
        vm.loadReport(reportId)

        val cats     = listOf("Alumbrado","Aseo","Seguridad","Servicios públicos","Vías","Otro")
        val prios    = listOf("BAJA","MEDIA","ALTA")
        val statuses = listOf("ABIERTO","EN_PROCESO","CERRADO")

        b.spinnerCategory.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, cats)
        b.spinnerPriority.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, prios)
        b.spinnerStatus.adapter   = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, statuses)

        vm.currentReport.observe(viewLifecycleOwner) { r ->
            if (r == null) return@observe
            b.etTitle.setText(r.title)
            b.etDescription.setText(r.description)
            b.etLocation.setText(r.location)
            b.spinnerCategory.setSelection(cats.indexOf(r.category).coerceAtLeast(0))
            b.spinnerPriority.setSelection(prios.indexOf(r.priority).coerceAtLeast(0))
            b.spinnerStatus.setSelection(statuses.indexOf(r.status).coerceAtLeast(0))

            b.btnSave.setOnClickListener {
                val title = b.etTitle.text.toString().trim()
                val desc  = b.etDescription.text.toString().trim()
                val loc   = b.etLocation.text.toString().trim()
                if (title.isEmpty() || desc.isEmpty() || loc.isEmpty()) {
                    Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                vm.updateReport(r.copy(
                    title       = title,
                    description = desc,
                    category    = b.spinnerCategory.selectedItem.toString(),
                    priority    = b.spinnerPriority.selectedItem.toString(),
                    status      = b.spinnerStatus.selectedItem.toString(),
                    location    = loc,
                    isSynced    = false
                ))
            }
        }

        vm.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "Guardado ✓", Toast.LENGTH_SHORT).show()
                vm.clearResult()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}