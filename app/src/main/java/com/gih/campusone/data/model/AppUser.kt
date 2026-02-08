package com.gih.campusone.data.model

import com.google.firebase.Timestamp

/**
 * User data model for CampusOne app
 * Stored in Firestore: users/{uid}
 */
data class AppUser(
    val uid: String = "",
    val email: String = "",
    val role: String = "student", // "student", "professor", or "admin"
    val name: String = "",
    val createdAt: Timestamp? = null
)

