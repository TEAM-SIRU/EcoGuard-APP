package com.nativelap.ecoguard.feature.menu.viewmodel

import androidx.compose.runtime.Immutable

sealed interface MenuUiState {
    data object Loading : MenuUiState

    data object Failed : MenuUiState

    @Immutable
    data class Content(
        val userName: String,
        val studentNumber: String?,
        val grade: Int?,
        val classNumber: Int?,
        val isWithdrawing: Boolean = false,
        val isWithdrawn: Boolean = false,
        val hasWithdrawalFailed: Boolean = false,
    ) : MenuUiState
}
