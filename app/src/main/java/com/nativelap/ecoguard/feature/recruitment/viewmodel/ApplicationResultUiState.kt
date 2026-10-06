package com.nativelap.ecoguard.feature.recruitment.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ApplicationResultUiState {
    data class Completed(
        val applicationOrder: Int,
        val appliedDateTime: String,
    ) : ApplicationResultUiState

    data object FilledWhileApplying : ApplicationResultUiState
}
