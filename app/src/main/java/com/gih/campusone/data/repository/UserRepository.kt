package com.gih.campusone.data.repository

import com.gih.campusone.data.Result
import com.gih.campusone.data.model.AppUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository for User data operations
 */
class UserRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Get user data by UID
     *
     * @param uid User ID
     * @return Result with AppUser on success
     */
    suspend fun getUser(uid: String): Result<AppUser> {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(AppUser::class.java)
                ?: return Result.Error(Exception("User not found"))

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get user role by UID
     *
     * @param uid User ID
     * @return User role string or null if not found
     */
    suspend fun getUserRole(uid: String): String? {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            document.getString("role")
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Update user profile
     *
     * @param uid User ID
     * @param updates Map of fields to update
     * @return Result with Unit on success
     */
    suspend fun updateUser(uid: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(uid)
                .update(updates)
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

