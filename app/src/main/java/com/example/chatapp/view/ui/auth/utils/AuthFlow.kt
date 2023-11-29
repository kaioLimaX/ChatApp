package com.example.chatapp.view.ui.auth.utils

sealed class AuthFlow {
    object Success : AuthFlow()
    data class Failure(val e: String) : AuthFlow()
    object Loading : AuthFlow()
}