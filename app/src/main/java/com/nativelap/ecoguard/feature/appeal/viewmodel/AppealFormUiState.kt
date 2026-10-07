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
) {
    val canSubmit: Boolean
        get() = appealContent.isNotBlank()
}
