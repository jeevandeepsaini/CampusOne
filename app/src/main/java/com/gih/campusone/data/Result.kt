package com.gih.campusone.data

/**
 * A sealed class to represent different states of data loading
 * Used for handling async operations with UI state management
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    /**
     * Check if the result is successful
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Check if the result is an error
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Check if the result is loading
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * Get data if success, null otherwise
     */
    fun getOrNull(): T? = if (this is Success) data else null

    /**
     * Get exception if error, null otherwise
     */
    fun exceptionOrNull(): Exception? = if (this is Error) exception else null
}

