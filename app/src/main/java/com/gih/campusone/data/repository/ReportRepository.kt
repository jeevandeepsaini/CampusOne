package com.gih.campusone.data.repository

import com.gih.campusone.data.Result
import com.gih.campusone.data.model.Report
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository for Report operations
 */
class ReportRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Add a new report
     *
     * @param category Report category
     * @param description Report description
     * @param location Optional location
     * @param createdByUid User ID of creator
     * @param createdByEmail Email of creator
     * @param createdByRole Role of creator
     * @return Result with report ID on success
     */
    suspend fun addReport(
        category: String,
        description: String,
        location: String = "",
        createdByUid: String,
        createdByEmail: String,
        createdByRole: String
    ): Result<String> {
        return try {
            val report = hashMapOf(
                "category" to category,
                "description" to description,
                "location" to location,
                "status" to "Pending",
                "createdAt" to Timestamp.now(),
                "updatedAt" to Timestamp.now(),
                "createdByUid" to createdByUid,
                "createdByEmail" to createdByEmail,
                "createdByRole" to createdByRole
            )

            val documentRef = firestore.collection("reports")
                .add(report)
                .await()

            // Update document with its own ID
            documentRef.update("id", documentRef.id).await()

            Result.Success(documentRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get real-time flow of reports created by specific user
     *
     * @param uid User ID
     * @return Flow emitting list of user's reports
     */
    fun myReports(uid: String): Flow<List<Report>> = callbackFlow {
        val listenerRegistration = firestore.collection("reports")
            .whereEqualTo("createdByUid", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val reports = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Report::class.java)?.copy(id = doc.id)
                    }
                    trySend(reports)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Get real-time flow of all reports (for admin)
     *
     * @return Flow emitting list of all reports
     */
    fun allReports(): Flow<List<Report>> = callbackFlow {
        val listenerRegistration = firestore.collection("reports")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val reports = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Report::class.java)?.copy(id = doc.id)
                    }
                    trySend(reports)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Update report status (admin only)
     *
     * @param reportId Report document ID
     * @param status New status value
     * @return Result with Unit on success
     */
    suspend fun updateStatus(reportId: String, status: String): Result<Unit> {
        return try {
            firestore.collection("reports")
                .document(reportId)
                .update(
                    mapOf(
                        "status" to status,
                        "updatedAt" to Timestamp.now()
                    )
                )
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get single report by ID
     *
     * @param reportId Report document ID
     * @return Result with Report on success
     */
    suspend fun getReport(reportId: String): Result<Report> {
        return try {
            val document = firestore.collection("reports")
                .document(reportId)
                .get()
                .await()

            val report = document.toObject(Report::class.java)?.copy(id = document.id)
                ?: return Result.Error(Exception("Report not found"))

            Result.Success(report)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Delete report (admin only)
     *
     * @param reportId Report document ID
     * @return Result with Unit on success
     */
    suspend fun deleteReport(reportId: String): Result<Unit> {
        return try {
            firestore.collection("reports")
                .document(reportId)
                .delete()
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

