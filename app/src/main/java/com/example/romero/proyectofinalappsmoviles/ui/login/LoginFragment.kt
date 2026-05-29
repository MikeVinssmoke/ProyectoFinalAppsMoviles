package com.example.romero.proyectofinalappsmoviles.ui.login



import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentLoginBinding
import com.example.romero.proyectofinalappsmoviles.util.Constants

class LoginFragment : Fragment() {
    private var _b: FragmentLoginBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentLoginBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.btnLogin.setOnClickListener {
            val user = b.etUsername.text.toString().trim()
            val pass = b.etPassword.text.toString().trim()

            if (user.isEmpty()) {
                b.tilUsername.error = "Campo obligatorio"
                return@setOnClickListener
            }
            if (pass.isEmpty()) {
                b.tilPassword.error = "Campo obligatorio"
                return@setOnClickListener
            }

            if (user == Constants.HARDCODED_USER && pass == Constants.HARDCODED_PASS) {
                val prefs = requireContext()
                    .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit()
                    .putBoolean(Constants.PREF_IS_LOGGED, true)
                    .putString(Constants.PREF_USERNAME, user)
                    .apply()
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Toast.makeText(requireContext(),
                    "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}