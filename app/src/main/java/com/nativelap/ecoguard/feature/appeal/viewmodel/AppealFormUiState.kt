package com.nativelap.ecoguard.feature.appeal.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class AppealFormUiState(
    val rejectionReason: String,
    val appealContent: String,
    val attachedPhotoCount: Int,
    val maxPhotoCount: Int,
    val maxContentLength: Int,
    val verificationDeadlineTime: String,
    val isSubmitting: Boolean = false,
    val isSubmissionFailed: Boolean = false,
    val isSubmitted: Boolean = false,
) {
    val canSubmit: Boolean
        get() = appealContent.isNotBlank() && !isSubmitting && !isSubmitted
}
