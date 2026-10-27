package com.nativelap.ecoguard.feature.verification.viewmodel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.nativelap.ecoguard.ui.component.StatusChipType

@Immutable
sealed interface VerificationDetailUiState {
    data object Loading : VerificationDetailUiState

    data object Failed : VerificationDetailUiState

    data class Content(
        val verificationDate: String,
        @StringRes val statusDescriptionRes: Int,
        val rejectionReason: String?,
        val status: StatusChipType,
        val areaName: String,
        val submittedDateTime: String,
    ) : VerificationDetailUiState
}
