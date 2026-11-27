package com.nativelap.ecoguard.feature.notification.viewmodel

sealed interface NotificationSettingsUiState {
    data object Loading : NotificationSettingsUiState

    data object Empty : NotificationSettingsUiState

    data object Failed : NotificationSettingsUiState

    data class Content(
        val settings: List<NotificationSettingUiModel>,
        val updatingCategory: String? = null,
    ) : NotificationSettingsUiState
}

data class NotificationSettingUiModel(
    val category: String,
    val label: String,
    val isEnabled: Boolean,
)
