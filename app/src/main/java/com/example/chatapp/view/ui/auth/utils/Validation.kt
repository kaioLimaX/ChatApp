package com.example.chatapp.view.ui.auth.utils

sealed class ValidationResult {
    data class ErrorEmail(val message: String) : ValidationResult()
    data class ErrorPassword(val message: String) : ValidationResult()
    data class ErrorName(val message: String) : ValidationResult()
}