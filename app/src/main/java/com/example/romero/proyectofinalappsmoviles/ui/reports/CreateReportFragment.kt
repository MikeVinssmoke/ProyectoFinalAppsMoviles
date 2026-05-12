package com.example.romero.proyectofinalappsmoviles.ui.reports

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReportEntity
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentCreateReportBinding
import com.example.romero.proyectofinalappsmoviles.viewmodel.ReportViewModel

class CreateReportFragment : Fragment() {
    private var _b: FragmentCreateReportBinding? = null
    private val b get() = _b!!
    private val vm: ReportViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentCreateReportBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cats = listOf("Alumbrado","Aseo","Seguridad","Servicios públicos","Vías","Otro")
        b.spinnerCategory.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, cats)

        val prios = listOf("BAJA","MEDIA","ALTA")
        b.spinnerPriority.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, prios)

        vm.operationResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "Reporte creado ✓", Toast.LENGTH_SHORT).show()
                vm.clearResult()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Error: ${result.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        b.btnSave.setOnClickListener        { saveReport() }
        b.btnSaveOffline.setOnClickListener { saveReport() }
    }

    private fun saveReport() {
        val title = b.etTitle.text.toString().trim()
        val desc  = b.etDescription.text.toString().trim()
        val loc   = b.etLocation.text.toString().trim()
        var ok = true
        if (title.isEmpty()) { b.tilTitle.error       = "Obligatorio"; ok = false } else b.tilTitle.error = null
        if (desc.isEmpty())  { b.tilDescription.error = "Obligatorio"; ok = false } else b.tilDescription.error = null
        if (loc.isEmpty())   { b.tilLocation.error    = "Obligatorio"; ok = false } else b.tilLocation.error = null
        if (!ok) return
        vm.createReport(ReportEntity(
            title       = title,
            description = desc,
            category    = b.spinnerCategory.selectedItem.toString(),
            priority    = b.spinnerPriority.selectedItem.toString(),
            location    = loc
        ))
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}