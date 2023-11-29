package com.example.chatapp.view.ui.auth.utils

sealed class AuthFlow {
    object Success : AuthFlow()
    data class Failure(val e: Exception) : AuthFlow()
    object Loading : AuthFlow()
}