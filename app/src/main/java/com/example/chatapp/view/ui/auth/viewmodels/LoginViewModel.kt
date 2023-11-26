package com.example.chatapp.view.ui.auth.viewmodels

import androidx.lifecycle.ViewModel
import com.example.chatapp.data.AuthRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun onClickLogin(email : String, password : String){
        authRepository.login(email,password)
    }
}