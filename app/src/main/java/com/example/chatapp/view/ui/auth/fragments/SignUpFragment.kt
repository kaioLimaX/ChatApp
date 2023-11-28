package com.example.chatapp.view.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.chatapp.data.Resource
import com.example.chatapp.databinding.ActivityAuthBinding
import com.example.chatapp.databinding.ItemTabloginSignupBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.example.chatapp.view.ui.auth.viewmodels.SignUpViewModel
import com.example.chatapp.view.ui.main.MainActivity
import com.example.chatapp.view.ui.utils.ActivityUtils
import com.wajahatkarim3.easyvalidation.core.view_ktx.contains
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings


@AndroidEntryPoint
@WithFragmentBindings
class SignUpFragment : Fragment() {

    private val signupViewModel: SignUpViewModel by viewModels()

    private lateinit var binding: ItemTabloginSignupBinding

    private var authBinding: ActivityAuthBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemTabloginSignupBinding.inflate(inflater, container, false)

        authBinding = (activity as? AuthActivity)?.binding

        signupViewModel.ValidateState.observe(viewLifecycleOwner){
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


        signupViewModel.signupFlow.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Failure -> {
                        if(it.e.message!!.contains("The email address is already in use by another account")){
                            binding.inputNome.isErrorEnabled = false
                            binding.inputemail.error = "The email address is already in use"
                            //empty
                        }

                        Log.i("info_auth", "Failure: ${it.e} ")
                        authBinding?.progressLogin?.visibility = View.GONE
                    }

                    is Resource.Loading -> authBinding?.progressLogin?.visibility = View.VISIBLE

                    is Resource.Sucess -> {
                        ActivityUtils.goToActivity(requireContext(), MainActivity::class.java)
                        authBinding?.progressLogin?.visibility = View.GONE
                        Log.i("info_auth", "Sucess: ${it.result.uid} ")
                    }

                    null -> toast("nulo")
                }
            }
        }

        binding.btnCriar.setOnClickListener {
            it.hideKeyboard()
            val name = binding.txtNome.text.toString()
            val email = binding.txtEmail.text.toString()
            val senha = binding.txtSenha.text.toString()

            signupViewModel.validateField(email, senha,name)

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


}