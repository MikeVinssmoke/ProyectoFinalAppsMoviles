package com.example.romero.proyectofinalappsmoviles.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentSettingsBinding
import com.example.romero.proyectofinalappsmoviles.util.Constants

class SettingsFragment : Fragment() {
    private var _b: FragmentSettingsBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSettingsBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = requireContext().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

        val themes   = listOf("Sistema","Claro","Oscuro")
        val notifs   = listOf("Activas","Silenciosas","Desactivadas")
        val statuses = listOf("Todos","ABIERTO","EN_PROCESO","CERRADO")

        b.spinnerTheme.adapter         = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, themes)
        b.spinnerNotifications.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, notifs)
        b.spinnerDefaultFilter.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statuses)

        b.spinnerTheme.setSelection(prefs.getInt(Constants.PREF_THEME, 0))
        b.spinnerNotifications.setSelection(prefs.getInt(Constants.PREF_NOTIFICATIONS, 0))
        b.spinnerDefaultFilter.setSelection(prefs.getInt(Constants.PREF_STATUS_FILTER, 0))

        b.btnSave.setOnClickListener {
            val themeIdx = b.spinnerTheme.selectedItemPosition
            prefs.edit()
                .putInt(Constants.PREF_THEME,         themeIdx)
                .putInt(Constants.PREF_NOTIFICATIONS, b.spinnerNotifications.selectedItemPosition)
                .putInt(Constants.PREF_STATUS_FILTER, b.spinnerDefaultFilter.selectedItemPosition)
                .apply()
            AppCompatDelegate.setDefaultNightMode(when (themeIdx) {
                1    -> AppCompatDelegate.MODE_NIGHT_NO
                2    -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            })
            Toast.makeText(requireContext(), "Preferencias guardadas ✓", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}