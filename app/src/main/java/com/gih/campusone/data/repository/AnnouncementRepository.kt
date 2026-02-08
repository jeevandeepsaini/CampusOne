package com.gih.campusone.data.repository

import com.gih.campusone.data.Result
import com.gih.campusone.data.model.Announcement
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository for Announcement operations
 */
class AnnouncementRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Get real-time flow of all announcements
     *
     * @return Flow emitting list of announcements
     */
    fun announcements(): Flow<List<Announcement>> = callbackFlow {
        val listenerRegistration = firestore.collection("announcements")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val announcements = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Announcement::class.java)?.copy(id = doc.id)
                    }
                    trySend(announcements)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Add a new announcement (admin only)
     *
     * @param title Announcement title
     * @param message Announcement message
     * @param createdByUid User ID of creator
     * @return Result with announcement ID on success
     */
    suspend fun addAnnouncement(
        title: String,
        message: String,
        createdByUid: String
    ): Result<String> {
        return try {
            val announcement = hashMapOf(
                "title" to title,
                "message" to message,
                "createdAt" to Timestamp.now(),
                "createdByUid" to createdByUid
            )

            val documentRef = firestore.collection("announcements")
                .add(announcement)
                .await()

            // Update document with its own ID
            documentRef.update("id", documentRef.id).await()

            Result.Success(documentRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Delete an announcement (admin only)
     *
     * @param announcementId Announcement document ID
     * @return Result with Unit on success
     */
    suspend fun deleteAnnouncement(announcementId: String): Result<Unit> {
        return try {
            firestore.collection("announcements")
                .document(announcementId)
                .delete()
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get single announcement by ID
     *
     * @param announcementId Announcement document ID
     * @return Result with Announcement on success
     */
    suspend fun getAnnouncement(announcementId: String): Result<Announcement> {
        return try {
            val document = firestore.collection("announcements")
                .document(announcementId)
                .get()
                .await()

            val announcement = document.toObject(Announcement::class.java)?.copy(id = document.id)
                ?: return Result.Error(Exception("Announcement not found"))

            Result.Success(announcement)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Update an announcement (admin only)
     *
     * @param announcementId Announcement document ID
     * @param title New title
     * @param message New message
     * @return Result with Unit on success
     */
    suspend fun updateAnnouncement(
        announcementId: String,
        title: String,
        message: String
    ): Result<Unit> {
        return try {
            firestore.collection("announcements")
                .document(announcementId)
                .update(
                    mapOf(
                        "title" to title,
                        "message" to message
                    )
                )
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

