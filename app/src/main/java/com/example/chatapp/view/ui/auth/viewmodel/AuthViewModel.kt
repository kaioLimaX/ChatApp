package com.example.chatapp.view.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.AuthRepository
import com.example.chatapp.data.Resource
import com.example.chatapp.view.ui.auth.utils.AuthFlow
import com.example.chatapp.view.ui.auth.utils.ValidationResult
import com.google.firebase.auth.FirebaseUser
import com.wajahatkarim3.easyvalidation.core.view_ktx.contains
import com.wajahatkarim3.easyvalidation.core.view_ktx.minLength
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authFlow = MutableLiveData<AuthFlow>()
    val authFlow: LiveData<AuthFlow>
        get() = _authFlow

    private val _validateState = MutableLiveData<ValidationResult>()
    val validateState: LiveData<ValidationResult>
        get() = _validateState

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    private val _selectedTabIndex = MutableLiveData<Int>()
    val selectedTabIndex: LiveData<Int>
        get() = _selectedTabIndex

    init {
        if (authRepository.currentUser != null) {
            _authFlow.value = AuthFlow.Success
            Log.i("info_auth", "${Resource.Sucess(authRepository.currentUser!!)} ")
        }
    }

    fun login(
        email: String,
        password: String

    ) = viewModelScope.launch {
        _authFlow.value = AuthFlow.Loading
        val response = authRepository.login(email, password)
        when (response) {
            is Resource.Failure -> {
                _authFlow.value = AuthFlow.Failure(response.e)
                Log.i("info_auth", "Failure: ${response.e} ")
            }

            is Resource.Sucess -> {
                _authFlow.value = AuthFlow.Success
                Log.i("info_auth", "Sucess: ${response.result.uid} ")

            }
        }


    }

    fun signup(
        email: String,
        password: String,
        name: String
    ) = viewModelScope.launch {
        _authFlow.postValue(AuthFlow.Loading)
        val response = authRepository.signup(email, password, name)
        when (response) {
            is Resource.Failure -> {
                Log.i("info_auth", "Failure: ${response.e} ")
                _authFlow.postValue(AuthFlow.Failure(response.e))
            }

            is Resource.Sucess -> {
                _authFlow.postValue(AuthFlow.Success)
                Log.i("info_auth", "Sucess: ${response.result.uid} ")
            }
        }


    }

    fun validateField(email: String, password: String) {
        val emailValidate = email.validEmail() && email.nonEmpty()
        val passwordValidate = password.minLength(6) && password.nonEmpty()

        if (emailValidate) {
            if (passwordValidate) {
                login(email, password)


            } else {
                _validateState.postValue(ValidationResult.ErrorPassword("Enter a valid password"))

            }

        } else {
            _validateState.postValue(ValidationResult.ErrorEmail("Enter a valid email"))

        }

    }

    fun validateField(email: String, password: String, name: String) {
        val emailValidate = email.validEmail() && email.nonEmpty()
        val passwordValidate = password.minLength(6) && password.nonEmpty()
        val nameValidate = name.nonEmpty() && name.minLength(4)

        if (nameValidate) {
            if (emailValidate) {
                if (passwordValidate) {
                    signup(email, password, name)


                } else {
                    _validateState.postValue(ValidationResult.ErrorPassword("Enter a valid password"))

                }

            } else {
                _validateState.postValue(ValidationResult.ErrorEmail("Enter a valid email"))

            }

        } else {
            _validateState.postValue(ValidationResult.ErrorName("Enter a valid name"))
        }
    }


}