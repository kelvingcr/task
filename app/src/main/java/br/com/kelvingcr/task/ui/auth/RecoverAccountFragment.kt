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
import br.com.kelvingcr.task.databinding.FragmentRecoverAccountBinding
import br.com.kelvingcr.task.ui.helper.BaseFragment
import br.com.kelvingcr.task.ui.helper.FirebaseHelper
import br.com.kelvingcr.task.ui.helper.initToolbar
import br.com.kelvingcr.task.ui.helper.showBottomSheet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initClicks()
    }

    private fun initClicks() {
        binding.btnSend.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim();

        if (email.isNotEmpty()) {
            binding.progressBar.isVisible = true
            recoveryUser(email)
        } else {
            showBottomSheet(message = R.string.text_email_empty_recover_account_fragment)
        }
    }

    private fun recoveryUser(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    hideKeyboard()
                    showBottomSheet(
                        message = R.string.text_email_send_sucess_recover_account_fragment
                    )
                } else {
                    showBottomSheet(
                        message = FirebaseHelper.validError(task.exception?.message ?: "")
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}