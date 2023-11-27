package com.example.chatapp.view.ui.auth.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.AuthRepository
import com.example.chatapp.data.Resource
import com.example.chatapp.view.ui.auth.fragments.ValidationResult
import com.google.firebase.auth.FirebaseUser
import com.wajahatkarim3.easyvalidation.core.view_ktx.minLength
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>()
    val loginFlow: LiveData<Resource<FirebaseUser>?>
        get() = _loginFlow

    private val _ValidateState = MutableLiveData<ValidationResult>()
    val ValidateState: LiveData<ValidationResult>
        get() = _ValidateState

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if (authRepository.currentUser != null) {
            _loginFlow.value = Resource.Sucess(authRepository.currentUser!!)
            Log.i("info_auth", "${Resource.Sucess(authRepository.currentUser!!)} ")
        }
    }

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginFlow.postValue(Resource.Loading)
        val response = authRepository.login(email, password)
        _loginFlow.postValue(response)
        Log.i("info_response", "$_loginFlow")

    }

    fun validateField(email: String, password: String){
        var emailValidate = email.validEmail() && email.nonEmpty()
        var passwordValidate = password.minLength(6) && password.nonEmpty()

        if(emailValidate){
            if(passwordValidate){
                login(email,password)


            }else{
                _ValidateState.postValue(ValidationResult.ErrorPassword("Enter a valid password"))

            }

        }else{
            _ValidateState.postValue(ValidationResult.ErrorEmail("Enter a valid email"))

        }

    }


}


