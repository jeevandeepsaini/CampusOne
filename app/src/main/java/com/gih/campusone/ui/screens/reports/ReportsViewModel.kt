package com.gih.campusone.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gih.campusone.data.Result
import com.gih.campusone.data.model.Report
import com.gih.campusone.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing report creation, viewing, and submission
 */
class ReportsViewModel(
    private val reportRepository: ReportRepository = ReportRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    /**
     * Update selected category
     */
    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            category = category,
            categoryError = null
        )
    }

    /**
     * Update description text
     */
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description,
            descriptionError = null
        )
    }

    /**
     * Update location text
     */
    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    /**
     * Submit report to Firestore
     */
    fun submitReport(
        userId: String,
        userEmail: String,
        userRole: String,
        onSuccess: () -> Unit
    ) {
        val currentState = _uiState.value

        // Validate inputs
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            when (val result = reportRepository.addReport(
                category = currentState.category,
                description = currentState.description.trim(),
                location = currentState.location.trim(),
                createdByUid = userId,
                createdByEmail = userEmail,
                createdByRole = userRole
            )) {
                is Result.Success -> {
                    _uiState.value = ReportUiState() // Reset form
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Failed to submit report"
                    )
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Get real-time flow of user's reports
     */
    fun getMyReports(userId: String) = reportRepository.myReports(userId)

    /**
     * Get real-time flow of all reports (admin only)
     */
    fun getAllReports() = reportRepository.allReports()

    /**
     * Update report status (admin only)
     */
    fun updateReportStatus(
        reportId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            when (val result = reportRepository.updateStatus(reportId, newStatus)) {
                is Result.Success -> {
                    onSuccess()
                }
                is Result.Error -> {
                    onError(result.exception.message ?: "Failed to update status")
                }
                is Result.Loading -> {
                    // Loading state
                }
            }
        }
    }

    /**
     * Get single report by ID
     */
    fun getReport(reportId: String) {
        viewModelScope.launch {
            when (val result = reportRepository.getReport(reportId)) {
                is Result.Success -> {
                    // Report loaded successfully
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.exception.message ?: "Failed to load report"
                    )
                }
                is Result.Loading -> {
                    // Loading state
                }
            }
        }
    }

    /**
     * Validate form inputs
     */
    private fun validateInputs(): Boolean {
        val currentState = _uiState.value
        var isValid = true

        // Validate category
        if (currentState.category.isBlank()) {
            _uiState.value = currentState.copy(
                categoryError = "Please select a category"
            )
            isValid = false
        }

        // Validate description
        if (currentState.description.isBlank()) {
            _uiState.value = currentState.copy(
                descriptionError = "Description is required"
            )
            isValid = false
        } else if (currentState.description.length < 10) {
            _uiState.value = currentState.copy(
                descriptionError = "Description must be at least 10 characters"
            )
            isValid = false
        }

        return isValid
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * UI state for report creation screen
 */
data class ReportUiState(
    val category: String = "",
    val description: String = "",
    val location: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categoryError: String? = null,
    val descriptionError: String? = null
)

