package com.nativelap.ecoguard.feature.appeal.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AppealResultUiState {
    data class Approved(
        val verificationDate: String,
        val attemptNumber: Int,
        val earnedMinutes: Int,
    ) : AppealResultUiState

    data class Rejected(
        val verificationDate: String,
        val attemptNumber: Int,
        val teacherResponseTitle: String,
        val teacherResponseDetail: String,
    ) : AppealResultUiState

    data object SendFailed : AppealResultUiState
}
