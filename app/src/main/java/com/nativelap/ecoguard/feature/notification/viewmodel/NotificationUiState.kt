package com.nativelap.ecoguard.feature.notification.viewmodel

sealed interface NotificationUiState {
    data object Loading : NotificationUiState

    data object Empty : NotificationUiState

    data object Failed : NotificationUiState

    data object NotIntegrated : NotificationUiState

    data class Content(
        val notifications: List<NotificationUiModel>,
    ) : NotificationUiState
}
