package com.gih.campusone.data.model

import com.google.firebase.Timestamp

/**
 * Report data model for issue reporting
 * Stored in Firestore: reports/{docId}
 */
data class Report(
    val id: String = "",
    val category: String = "",
    val description: String = "",
    val location: String = "",
    val status: String = "Pending", // "Pending", "In Progress", "Resolved"
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val createdByUid: String = "",
    val createdByEmail: String = "",
    val createdByRole: String = ""
)

/**
 * Report categories
 */
object ReportCategory {
    const val INFRASTRUCTURE = "Infrastructure"
    const val HYGIENE = "Hygiene"
    const val SECURITY = "Security"
    const val NETWORK = "Network"
    const val OTHER = "Other"

    val ALL = listOf(INFRASTRUCTURE, HYGIENE, SECURITY, NETWORK, OTHER)
}

/**
 * Report status values
 */
object ReportStatus {
    const val PENDING = "Pending"
    const val IN_PROGRESS = "In Progress"
    const val RESOLVED = "Resolved"

    val ALL = listOf(PENDING, IN_PROGRESS, RESOLVED)
}

