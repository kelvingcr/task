package br.com.kelvingcr.task.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.kelvingcr.task.R
import br.com.kelvingcr.task.databinding.FragmentSplashBinding
import br.com.kelvingcr.task.ui.helper.FirebaseHelper
import com.google.firebase.ktx.Firebase

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(
            this::checkAuth,
            3000
        ) //depois de 3 segundos ele chama a função

    }

    private fun checkAuth() {
        if (FirebaseHelper.isAutenticated()) {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment2)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_autentication)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}