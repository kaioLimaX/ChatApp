package com.example.chatapp.view.ui.auth.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.AuthRepository
import com.example.chatapp.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)

    val loginFlow : StateFlow<Resource<FirebaseUser>?> = _loginFlow

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if(authRepository.currentUser != null){
            _loginFlow.value = Resource.Sucess(authRepository.currentUser!!)
            Log.i("info_auth", ": ${Resource.Sucess(authRepository.currentUser!!)} ")
        }
    }

    fun onClickLogin(
        email: String,
        password: String  ) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
           val response =  authRepository.login(email,password)
        _loginFlow.value = response

    }



}


