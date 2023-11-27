package com.example.chatapp.view.ui.auth.fragments

import java.lang.Exception

sealed class ValidationResult {
    object Success : ValidationResult()
    data class ErrorEmail(val message: String) : ValidationResult()
    data class ErrorPassword(val message: String) : ValidationResult()
}