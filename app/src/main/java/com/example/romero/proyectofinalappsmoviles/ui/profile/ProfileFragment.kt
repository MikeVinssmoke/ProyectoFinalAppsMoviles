package com.example.romero.proyectofinalappsmoviles.ui.profile



import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentProfileBinding
import com.example.romero.proyectofinalappsmoviles.util.Constants

class ProfileFragment : Fragment() {
    private var _b: FragmentProfileBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentProfileBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs = requireContext()
            .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        val username = prefs.getString(Constants.PREF_USERNAME, "Usuario") ?: "Usuario"

        b.tvUsername.text = username
        b.tvFullName.text = "Bienvenido, $username"

        b.optionMyInfo.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }
        b.optionHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Próximamente", Toast.LENGTH_SHORT).show()
        }
        b.optionLogout.setOnClickListener {
            prefs.edit()
                .putBoolean(Constants.PREF_IS_LOGGED, false)
                .remove(Constants.PREF_USERNAME)
                .apply()
            findNavController().navigate(R.id.action_profile_to_login)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}