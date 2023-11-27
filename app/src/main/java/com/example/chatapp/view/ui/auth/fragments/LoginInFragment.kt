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
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
@WithFragmentBindings
class LoginInFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var binding: ItemTabloginLoginBinding

    private var fragmentBinding: ActivityAuthBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemTabloginLoginBinding.inflate(inflater, container, false)

        fragmentBinding = (activity as? AuthActivity)?.binding


        loginViewModel.loginFlow.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Failure -> {
                        toast("falha ao fazer login")
                        fragmentBinding?.progressLogin?.visibility = View.GONE
                    }

                    is Resource.Loading -> fragmentBinding?.progressLogin?.visibility = View.VISIBLE

                    is Resource.Sucess -> {
                        toast("sucesso ao fazer login")
                        fragmentBinding?.progressLogin?.visibility = View.GONE
                    }

                    null -> toast("nulo")
                }
            }
        }

        binding.btnLogar.setOnClickListener {
            it.hideKeyboard()
            val email = binding.txtEmail.text.toString()
            val senha = binding.txtSenha.text.toString()
            loginViewModel.onClickLogin(email, senha)

        }



        return binding.getRoot()
    }


    fun toast(message: String) {
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }

    /*
        private fun validarCampos(email: String, senha: String): Boolean {
            val formatoEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if (email.isNotEmpty() && formatoEmail) {
                if (senha.isNotEmpty() && senha.length >= 8) {
                    return true

                } else {
                    binding.inputLoginSenha.error = "insira um senha mais forte"
                 //   mainBinding?.pbLoading?.visibility = View.INVISIBLE
                    return false
                }

            } else {
                binding.inputLoginEmail.error = "insira um email valido"
               // mainBinding?.pbLoading?.visibility = View.INVISIBLE
                return false
            }



        }*/

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }


}