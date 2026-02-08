package com.gih.campusone.ui.screens.auth

/**
 * UI state for authentication-related screens
 * Used by Login and SignUp screens
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val selectedRole: String = "student",
    val emailError: String? = null,
    val passwordError: String? = null
)

