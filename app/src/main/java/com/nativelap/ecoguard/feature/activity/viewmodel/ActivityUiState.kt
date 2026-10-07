package com.nativelap.ecoguard.feature.activity.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ActivityUiState {
    data object Loading : ActivityUiState

    data object LoadFailed : ActivityUiState

    data class Content(
        val year: Int,
        val month: Int,
        val monthlyMinutes: Int,
        val approvedCount: Int,
        val rejectedCount: Int,
        val notSubmittedCount: Int,
        val weekGroups: List<ActivityWeekGroupUiModel>,
    ) : ActivityUiState
}
