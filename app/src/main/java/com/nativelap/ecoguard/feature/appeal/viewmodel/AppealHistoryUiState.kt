package com.nativelap.ecoguard.feature.appeal.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AppealHistoryUiState {
    data object Loading : AppealHistoryUiState

    data object Empty : AppealHistoryUiState

    data object LoadFailed : AppealHistoryUiState

    data class Content(
        val items: List<AppealHistoryItemUiModel>,
    ) : AppealHistoryUiState
}
