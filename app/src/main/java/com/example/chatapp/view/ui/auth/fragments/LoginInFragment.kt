package com.example.chatapp.view.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chatapp.databinding.ActivityAuthBinding
import com.example.chatapp.databinding.ItemTabloginLoginBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.example.chatapp.view.ui.auth.AuthConstants
import com.example.chatapp.view.ui.auth.utils.AuthFlow
import com.example.chatapp.view.ui.auth.utils.ValidationResult
import com.example.chatapp.view.ui.auth.viewmodel.AuthViewModel
import com.example.chatapp.view.ui.main.MainActivity
import com.example.chatapp.view.ui.main.utils.ActivityUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class LoginInFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var binding: ItemTabloginLoginBinding

    private var authBinding: ActivityAuthBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemTabloginLoginBinding.inflate(inflater, container, false)

        authBinding = (activity as? AuthActivity)?.binding

        authViewModel.validateState.observe(viewLifecycleOwner){
            when(it){
                is ValidationResult.ErrorEmail -> binding.inputLoginEmail.error = it.message
                is ValidationResult.ErrorPassword -> {
                    binding.inputLoginEmail.isErrorEnabled = false
                    binding.inputLoginSenha.error = it.message
                }
                is ValidationResult.ErrorName -> {}

            }

        }


        authViewModel.authFlow.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is AuthFlow.Failure -> {
                        hideProgressBar(authBinding!!.progressLogin)
                        toast(AuthConstants.msgErrorLogin)
                    }
                    AuthFlow.Loading -> showProgressBar(authBinding!!.progressLogin)
                    AuthFlow.Success -> {
                        hideProgressBar(authBinding!!.progressLogin)
                        ActivityUtils.goToActivity(requireContext(), MainActivity::class.java)
                        requireActivity().finish()
                    }
                }
            }
        }

        binding.btnLogar.setOnClickListener {
            it.hideKeyboard()
            val email = binding.txtEmail.text.toString()
            val senha = binding.txtSenha.text.toString()
            authViewModel.validateField(email, senha)

        }

        binding.txtSenha.addTextChangedListener {
            cleanError(binding.inputLoginSenha)
        }

        binding.txtEmail.addTextChangedListener {
            cleanError(binding.inputLoginEmail)
        }



        return binding.getRoot()
    }




    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

    fun cleanError(textInputLayout: TextInputLayout){
        textInputLayout.isErrorEnabled = false
    }


}