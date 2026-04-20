package com.vestbem.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vestbem.app.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnEntrar.setOnClickListener { fazerLogin() }
        binding.btnCadastrar.setOnClickListener { 
            findNavController().navigate(R.id.action_login_to_register)
        }
        
        observeViewModel()
    }
    
    private fun fazerLogin() {
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString().trim()
        
        if (validarCampos(email, senha)) {
            viewModel.login(email, senha)
        }
    }
    
    private fun validarCampos(email: String, senha: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.tilEmail.error = "Email é obrigatório"
                false
            }
            senha.isEmpty() -> {
                binding.tilSenha.error = "Senha é obrigatória"
                false
            }
            senha.length < 6 -> {
                binding.tilSenha.error = "Senha deve ter pelo menos 6 caracteres"
                false
            }
            else -> true
        }
    }
    
    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { userId ->
                Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_login_to_home)
            }.onFailure { error ->
                Toast.makeText(context, "Erro: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
