package br.com.kelvingcr.task.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import br.com.kelvingcr.task.R
import br.com.kelvingcr.task.databinding.FragmentRegisterBinding
import br.com.kelvingcr.task.databinding.FragmentSplashBinding
import br.com.kelvingcr.task.ui.helper.BaseFragment
import br.com.kelvingcr.task.ui.helper.FirebaseHelper
import br.com.kelvingcr.task.ui.helper.initToolbar
import br.com.kelvingcr.task.ui.helper.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : BaseFragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initClicks();
    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim();
        val password = binding.edtPassword.text.toString().trim();

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                binding.progressBar.isVisible = true
                registerUser(email, password)
            } else {
                showBottomSheet(message = R.string.text_password_empty_register_fragment)
            }
        } else {
            showBottomSheet(message = R.string.text_email_empty_register_fragment)
        }
    }

    private fun initClicks(){
        binding.btnRegister.setOnClickListener { validateData() }
    }

    private fun registerUser(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    showBottomSheet(
                        message = FirebaseHelper.validError(
                            task.exception?.message ?: ""
                        )
                    )
                    binding.progressBar.isVisible = false
                    println(task.exception)
                }

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}