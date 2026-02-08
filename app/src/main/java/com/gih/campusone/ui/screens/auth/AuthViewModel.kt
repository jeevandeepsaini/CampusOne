package com.gih.campusone.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gih.campusone.data.Result
import com.gih.campusone.data.repository.AuthRepository
import com.gih.campusone.data.repository.UserRepository
import com.gih.campusone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing authentication state and operations
 */
class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Check current auth state on initialization
        checkCurrentUser()
    }

    /**
     * Check if user is already logged in on app start
     * If logged in, fetch role from Firestore and compute admin status
     */
    private fun checkCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // User is logged in, fetch role from Firestore
                loadUserRoleAndSetState(currentUser.uid, currentUser.email ?: "")
            } else {
                // User is not logged in
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    /**
     * Login user with email and password
     *
     * @param email User email
     * @param password User password
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _errorMessage.value = null

            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    val user = result.data
                    // Fetch user role from Firestore
                    loadUserRoleAndSetState(user.uid, user.email ?: email)
                }
                is Result.Error -> {
                    _authState.value = AuthState.Unauthenticated
                    _errorMessage.value = result.exception.message ?: "Login failed"
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Sign up new user with email, password and role
     * Creates Firebase Auth user and Firestore user document
     *
     * @param email User email
     * @param password User password
     * @param role User role (student, professor)
     * @param name Optional user name
     */
    fun signup(email: String, password: String, role: String, name: String = "") {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _errorMessage.value = null

            when (val result = authRepository.signUp(email, password, role, name)) {
                is Result.Success -> {
                    val appUser = result.data
                    // Check if user is admin based on email
                    val isAdmin = email in Constants.ADMIN_EMAILS

                    _authState.value = AuthState.Authenticated(
                        uid = appUser.uid,
                        email = appUser.email,
                        role = appUser.role,
                        isAdmin = isAdmin
                    )
                }
                is Result.Error -> {
                    _authState.value = AuthState.Unauthenticated
                    _errorMessage.value = result.exception.message ?: "Signup failed"
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Logout current user
     */
    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.logout()
            _authState.value = AuthState.Unauthenticated
            _errorMessage.value = null
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Load user role from Firestore and set authenticated state
     *
     * @param uid User ID
     * @param email User email
     */
    private suspend fun loadUserRoleAndSetState(uid: String, email: String) {
        when (val result = userRepository.getUser(uid)) {
            is Result.Success -> {
                val appUser = result.data
                // Check if user is admin based on email
                val isAdmin = email in Constants.ADMIN_EMAILS

                _authState.value = AuthState.Authenticated(
                    uid = appUser.uid,
                    email = appUser.email,
                    role = appUser.role,
                    isAdmin = isAdmin
                )
            }
            is Result.Error -> {
                // If user document doesn't exist, create it with default role
                // This handles edge case where auth user exists but Firestore doc doesn't
                _authState.value = AuthState.Authenticated(
                    uid = uid,
                    email = email,
                    role = Constants.UserRole.STUDENT,
                    isAdmin = email in Constants.ADMIN_EMAILS
                )
            }
            is Result.Loading -> {
                // Should not happen
            }
        }
    }

    /**
     * Reload current user data
     * Useful after profile updates
     */
    fun reloadUser() {
        checkCurrentUser()
    }

    /**
     * Check if current user is admin
     */
    fun isAdmin(): Boolean {
        return when (val state = _authState.value) {
            is AuthState.Authenticated -> state.isAdmin
            else -> false
        }
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return when (val state = _authState.value) {
            is AuthState.Authenticated -> state.uid
            else -> null
        }
    }

    /**
     * Get current user email
     */
    fun getCurrentUserEmail(): String? {
        return when (val state = _authState.value) {
            is AuthState.Authenticated -> state.email
            else -> null
        }
    }

    /**
     * Get current user role
     */
    fun getCurrentUserRole(): String? {
        return when (val state = _authState.value) {
            is AuthState.Authenticated -> state.role
            else -> null
        }
    }
}

