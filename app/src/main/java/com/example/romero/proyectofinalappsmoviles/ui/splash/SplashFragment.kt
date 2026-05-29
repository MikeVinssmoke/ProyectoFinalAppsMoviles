package com.example.romero.proyectofinalappsmoviles.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.databinding.FragmentSplashBinding

import android.content.Context
import com.example.romero.proyectofinalappsmoviles.util.Constants

class SplashFragment : Fragment() {
    private var _b: FragmentSplashBinding? = null
    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSplashBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.logoContainer.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        )
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = requireContext()
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
            val isLogged = prefs.getBoolean(Constants.PREF_IS_LOGGED, false)
            if (isLogged) {
                findNavController().navigate(R.id.action_splash_to_home)
            } else {
                findNavController().navigate(R.id.action_splash_to_login)
            }
        }, 2000)
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}