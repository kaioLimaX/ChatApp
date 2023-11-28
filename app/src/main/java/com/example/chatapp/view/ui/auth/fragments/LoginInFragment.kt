package com.example.chatapp.view.ui.auth.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.data.Resource
import com.example.chatapp.databinding.ActivityAuthBinding
import com.example.chatapp.databinding.ItemTabloginLoginBinding
import com.example.chatapp.view.ui.auth.AuthActivity
import com.example.chatapp.view.ui.auth.viewmodels.LoginViewModel
import com.example.chatapp.view.ui.main.MainActivity
import com.example.chatapp.view.ui.utils.ActivityUtils
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.wajahatkarim3.easyvalidation.core.view_ktx.contains
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
@WithFragmentBindings
class LoginInFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var binding: ItemTabloginLoginBinding

    private var authBinding: ActivityAuthBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemTabloginLoginBinding.inflate(inflater, container, false)

        authBinding = (activity as? AuthActivity)?.binding

        loginViewModel.ValidateState.observe(viewLifecycleOwner){
            when(it){
                is ValidationResult.ErrorEmail -> binding.inputLoginEmail.error = it.message
                is ValidationResult.ErrorPassword -> {
                    binding.inputLoginEmail.isErrorEnabled = false
                    binding.inputLoginSenha.error = it.message
                }
                is ValidationResult.ErrorName -> {}

            }

        }


        loginViewModel.loginFlow.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Failure -> {
                    if(it.e.message!!.contains("There is no user record corresponding to this identifier")){

                        val tab: TabLayout.Tab? = authBinding!!.tabAuth.getTabAt(1)

                        if (tab != null) {
                            tab.select() // Move para a segunda aba
                        }
                        toast("unregistered user, register now")
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

        binding.btnLogar.setOnClickListener {
            it.hideKeyboard()
            val email = binding.txtEmail.text.toString()
            val senha = binding.txtSenha.text.toString()
            loginViewModel.validateField(email, senha)

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