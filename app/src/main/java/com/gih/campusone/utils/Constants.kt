package com.gih.campusone.utils

/**
 * Application-wide constants
 */
object Constants {

    /**
     * Admin email allowlist
     * Users with these emails will have admin privileges
     */
    val ADMIN_EMAILS = setOf(
        "admin@campusone.com",
        "admin@gih.edu",
        "campusone.admin@gmail.com"
    )

    /**
     * User roles
     */
    object UserRole {
        const val STUDENT = "student"
        const val PROFESSOR = "professor"
        const val ADMIN = "admin"
    }

    /**
     * Emergency contacts for SOS screen
     */
    object EmergencyContacts {
        const val SECURITY_NAME = "Campus Security"
        const val SECURITY_PHONE = "+91-XXX-XXX-XXXX"
        const val SECURITY_EMAIL = "security@campus.edu"

        const val MEDICAL_NAME = "Medical Emergency"
        const val MEDICAL_PHONE = "+91-XXX-XXX-XXXX"
        const val MEDICAL_EMAIL = "medical@campus.edu"

        const val FIRE_NAME = "Fire Department"
        const val FIRE_PHONE = "101"
        const val FIRE_EMAIL = "fire@campus.edu"

        const val POLICE_NAME = "Police"
        const val POLICE_PHONE = "100"
        const val POLICE_EMAIL = "police@campus.edu"
    }

    /**
     * Firestore collection names
     */
    object Collections {
        const val USERS = "users"
        const val REPORTS = "reports"
        const val ANNOUNCEMENTS = "announcements"
    }

    /**
     * Validation constants
     */
    object Validation {
        const val MIN_PASSWORD_LENGTH = 6
        const val MAX_DESCRIPTION_LENGTH = 500
        const val MAX_TITLE_LENGTH = 100
    }
}

