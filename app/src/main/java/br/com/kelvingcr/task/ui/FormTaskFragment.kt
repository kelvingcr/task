package br.com.kelvingcr.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.kelvingcr.task.R
import br.com.kelvingcr.task.databinding.FragmentFormTaskBinding
import br.com.kelvingcr.task.databinding.FragmentSplashBinding

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}