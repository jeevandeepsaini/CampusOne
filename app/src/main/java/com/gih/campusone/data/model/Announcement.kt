package com.gih.campusone.data.model

import com.google.firebase.Timestamp

/**
 * Announcement data model for campus announcements/events
 * Stored in Firestore: announcements/{docId}
 */
data class Announcement(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val createdAt: Timestamp? = null,
    val createdByUid: String = ""
)

