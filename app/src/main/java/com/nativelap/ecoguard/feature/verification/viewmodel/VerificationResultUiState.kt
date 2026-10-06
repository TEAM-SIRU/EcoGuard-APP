package com.nativelap.ecoguard.feature.verification.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface VerificationResultUiState {
    data object LoadFailed : VerificationResultUiState

    data class Approved(
        val areaName: String,
        val submittedTime: String,
        val earnedMinutes: Int,
    ) : VerificationResultUiState

    data class TeacherReviewing(
        val areaName: String,
        val submittedTime: String,
    ) : VerificationResultUiState

    data class Rejected(
        val areaName: String,
        val submittedTime: String,
        val rejectionReason: String,
        val retakeGuide: String,
    ) : VerificationResultUiState
}
