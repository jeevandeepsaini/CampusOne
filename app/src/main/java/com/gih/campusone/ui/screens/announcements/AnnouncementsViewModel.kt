package com.gih.campusone.ui.screens.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gih.campusone.data.Result
import com.gih.campusone.data.repository.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing announcements (viewing and admin operations)
 */
class AnnouncementsViewModel(
    private val announcementRepository: AnnouncementRepository = AnnouncementRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnnouncementUiState())
    val uiState: StateFlow<AnnouncementUiState> = _uiState.asStateFlow()

    /**
     * Get real-time flow of all announcements
     */
    fun getAnnouncements() = announcementRepository.announcements()

    /**
     * Add new announcement (admin only)
     */
    fun addAnnouncement(
        title: String,
        message: String,
        createdByUid: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Validate inputs
        if (!validateInputs(title, message)) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = announcementRepository.addAnnouncement(
                title = title.trim(),
                message = message.trim(),
                createdByUid = createdByUid
            )) {
                is Result.Success -> {
                    _uiState.value = AnnouncementUiState() // Reset form
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Failed to add announcement"
                    )
                    onError(_uiState.value.errorMessage ?: "Failed to add announcement")
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Delete announcement (admin only)
     */
    fun deleteAnnouncement(
        announcementId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            when (val result = announcementRepository.deleteAnnouncement(announcementId)) {
                is Result.Success -> {
                    onSuccess()
                }
                is Result.Error -> {
                    onError(result.exception.message ?: "Failed to delete announcement")
                }
                is Result.Loading -> {
                    // Loading state
                }
            }
        }
    }

    /**
     * Validate announcement inputs
     */
    private fun validateInputs(title: String, message: String): Boolean {
        var isValid = true

        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(
                titleError = "Title is required"
            )
            isValid = false
        } else if (title.length < 3) {
            _uiState.value = _uiState.value.copy(
                titleError = "Title must be at least 3 characters"
            )
            isValid = false
        }

        if (message.isBlank()) {
            _uiState.value = _uiState.value.copy(
                messageError = "Message is required"
            )
            isValid = false
        } else if (message.length < 10) {
            _uiState.value = _uiState.value.copy(
                messageError = "Message must be at least 10 characters"
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

    /**
     * Update title
     */
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title, titleError = null)
    }

    /**
     * Update message
     */
    fun updateMessage(message: String) {
        _uiState.value = _uiState.value.copy(message = message, messageError = null)
    }
}

/**
 * UI state for announcements screen
 */
data class AnnouncementUiState(
    val title: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val titleError: String? = null,
    val messageError: String? = null
)
