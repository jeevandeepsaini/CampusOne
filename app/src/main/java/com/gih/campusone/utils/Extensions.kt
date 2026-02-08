package com.gih.campusone.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Extension functions for various types used in the app
 */

/**
 * Format Firebase Timestamp to readable date string
 */
fun Timestamp?.toFormattedDate(): String {
    if (this == null) return ""
    val date = this.toDate()
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}

/**
 * Format Firebase Timestamp to readable date and time string
 */
fun Timestamp?.toFormattedDateTime(): String {
    if (this == null) return ""
    val date = this.toDate()
    val formatter = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
    return formatter.format(date)
}

/**
 * Format Firebase Timestamp to relative time (e.g., "2 hours ago")
 */
fun Timestamp?.toRelativeTime(): String {
    if (this == null) return ""
    val now = System.currentTimeMillis()
    val timestamp = this.toDate().time
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000} minutes ago"
        diff < 86400_000 -> "${diff / 3600_000} hours ago"
        diff < 604800_000 -> "${diff / 86400_000} days ago"
        else -> toFormattedDate()
    }
}

/**
 * Validate email format
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Validate password strength
 */
fun String.isValidPassword(): Boolean {
    return this.length >= Constants.Validation.MIN_PASSWORD_LENGTH
}

/**
 * Capitalize first letter of each word
 */
fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}

/**
 * Truncate string to max length with ellipsis
 */
fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        "${this.substring(0, maxLength)}..."
    } else {
        this
    }
}


