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
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signupFlow = MutableLiveData<Resource<FirebaseUser>?>()
    val signupFlow: LiveData<Resource<FirebaseUser>?>
        get() = _signupFlow

    private val _ValidateState = MutableLiveData<ValidationResult>()
    val ValidateState: LiveData<ValidationResult>
        get() = _ValidateState


    fun signup(
        email: String,
        password: String,
        name: String
    ) = viewModelScope.launch {
        _signupFlow.postValue(Resource.Loading)
        val response = authRepository.signup(email, password, name)
        _signupFlow.postValue(response)
        Log.i("info_response", "$_signupFlow")

    }

    fun validateField(email: String, password: String, name: String) {
        var emailValidate = email.validEmail() && email.nonEmpty()
        var passwordValidate = password.minLength(6) && password.nonEmpty()
        val nameValidate = name.nonEmpty() && name.minLength(4)

        if (nameValidate) {
            if (emailValidate) {
                if (passwordValidate) {
                    signup(email, password, name)


                } else {
                    _ValidateState.postValue(ValidationResult.ErrorPassword("Enter a valid password"))

                }

            } else {
                _ValidateState.postValue(ValidationResult.ErrorEmail("Enter a valid email"))

            }

        } else {
            _ValidateState.postValue(ValidationResult.ErrorName("Enter a valid name"))
        }
    }


}