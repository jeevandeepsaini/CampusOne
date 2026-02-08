package com.gih.campusone.data.repository

import com.gih.campusone.data.Result
import com.gih.campusone.data.model.AppUser
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository for Firebase Authentication operations
 */
class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Flow that emits current Firebase user
     * Emits null when user is logged out
     */
    val currentUser: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)

        // Emit current state immediately
        trySend(auth.currentUser)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Get current user email
     */
    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean = auth.currentUser != null

    /**
     * Sign up new user with email and password
     * Creates user document in Firestore after successful authentication
     *
     * @param email User email
     * @param password User password
     * @param role User role (student, professor, admin)
     * @param name Optional user name
     * @return Result with AppUser on success
     */
    suspend fun signUp(
        email: String,
        password: String,
        role: String,
        name: String = ""
    ): Result<AppUser> {
        return try {
            // Create Firebase Auth user
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error(Exception("Failed to create user"))

            // Create user document in Firestore
            val appUser = AppUser(
                uid = firebaseUser.uid,
                email = email,
                role = role,
                name = name,
                createdAt = Timestamp.now()
            )

            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(appUser)
                .await()

            Result.Success(appUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Login user with email and password
     *
     * @param email User email
     * @param password User password
     * @return Result with FirebaseUser on success
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error(Exception("Login failed"))

            Result.Success(firebaseUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Logout current user
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Send password reset email
     *
     * @param email User email
     * @return Result with Unit on success
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

