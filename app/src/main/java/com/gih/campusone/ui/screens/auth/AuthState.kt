package com.gih.campusone.ui.screens.auth

/**
 * Represents the authentication state of the user
 */
sealed class AuthState {
    /**
     * Loading state - checking authentication or performing auth operations
     */
    object Loading : AuthState()

    /**
     * User is not authenticated
     */
    object Unauthenticated : AuthState()

    /**
     * User is authenticated with role and admin status
     *
     * @param uid User ID
     * @param email User email
     * @param role User role (student, professor, admin)
     * @param isAdmin Whether user has admin privileges (based on ADMIN_EMAILS)
     */
    data class Authenticated(
        val uid: String,
        val email: String,
        val role: String,
        val isAdmin: Boolean
    ) : AuthState()
}

