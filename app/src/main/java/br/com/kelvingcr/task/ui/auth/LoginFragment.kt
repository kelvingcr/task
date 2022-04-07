package br.com.kelvingcr.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import br.com.kelvingcr.task.R
import br.com.kelvingcr.task.databinding.FragmentLoginBinding
import br.com.kelvingcr.task.ui.helper.BaseFragment
import br.com.kelvingcr.task.ui.helper.FirebaseHelper
import br.com.kelvingcr.task.ui.helper.showBottomSheet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCliques()
    }

    private fun initCliques(){
        binding.txtRecover.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
        binding.txtRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim();
        val password = binding.edtPassword.text.toString().trim();

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                hideKeyboard()
                binding.progressBar.isVisible = true
                loginUser(email, password)
            } else {
                showBottomSheet(
                    message = R.string.text_password_empty_login_fragment
                )
            }
        } else {
            showBottomSheet(
                message = R.string.text_email_empty_login_fragment
            )
        }
    }

    private fun loginUser(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    Toast.makeText(requireContext(), FirebaseHelper.validError(task.exception?.message ?: ""), Toast.LENGTH_SHORT).show()
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