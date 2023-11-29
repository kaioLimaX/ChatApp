package com.example.chatapp.view.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chatapp.databinding.ActivityAuthBinding
import com.example.chatapp.databinding.ItemTabloginSignupBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.example.chatapp.view.ui.auth.utils.AuthFlow
import com.example.chatapp.view.ui.auth.utils.ValidationResult
import com.example.chatapp.view.ui.auth.viewmodel.AuthViewModel
import com.example.chatapp.view.ui.main.MainActivity
import com.example.chatapp.view.ui.main.utils.ActivityUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings


@AndroidEntryPoint
@WithFragmentBindings
class SignUpFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()


    private lateinit var binding: ItemTabloginSignupBinding

    private var authBinding: ActivityAuthBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemTabloginSignupBinding.inflate(inflater, container, false)

        authBinding = (activity as? AuthActivity)?.binding

        authViewModel.ValidateState.observe(viewLifecycleOwner){
            when(it){
                is ValidationResult.ErrorEmail -> {
                    binding.inputNome.isErrorEnabled = false
                    binding.inputemail.error = it.message
                }
                is ValidationResult.ErrorPassword -> {
                    binding.inputNome.isErrorEnabled = false
                    binding.inputemail.isErrorEnabled = false
                    binding.inputSenha.error = it.message
                }
                is ValidationResult.ErrorName -> {
                    binding.inputNome.error = it.message
                }

            }

        }


        authViewModel.authFlow.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is AuthFlow.Failure -> {
                        hideProgressBar(authBinding!!.progressLogin)
                        toast(it.e)
                    }
                    AuthFlow.Loading -> showProgressBar(authBinding!!.progressLogin)
                    AuthFlow.Success -> {
                        hideProgressBar(authBinding!!.progressLogin)
                        ActivityUtils.goToActivity(requireContext(), MainActivity::class.java)
                    }
                }
            }
        }

        binding.btnCriar.setOnClickListener {
            it.hideKeyboard()
            val name = binding.txtNome.text.toString()
            val email = binding.txtEmail.text.toString()
            val senha = binding.txtSenha.text.toString()

            authViewModel.validateField(email, senha,name)

        }



        return binding.getRoot()
    }


    fun toast(message: String) {
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }


}