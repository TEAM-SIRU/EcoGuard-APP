package com.nativelap.ecoguard.feature.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationSetting
import com.nativelap.ecoguard.feature.notification.module.domain.usecase.GetNotificationSettingsUseCase
import com.nativelap.ecoguard.feature.notification.module.domain.usecase.UpdateNotificationSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel
    @Inject
    constructor(
        private val getNotificationSettings: GetNotificationSettingsUseCase,
        private val updateNotificationSetting: UpdateNotificationSettingUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<NotificationSettingsUiState>(NotificationSettingsUiState.Loading)
        val uiState = mutableUiState.asStateFlow()
        private val mutableEffects = MutableSharedFlow<NotificationSettingsEffect>()
        val effects = mutableEffects.asSharedFlow()

        init {
            loadSettings()
        }

        fun retry() {
            loadSettings()
        }

        fun updateSetting(
            category: String,
            isEnabled: Boolean,
        ) {
            val currentState = mutableUiState.value as? NotificationSettingsUiState.Content ?: return
            if (currentState.updatingCategory != null) return
            mutableUiState.value = currentState.copy(updatingCategory = category)
            viewModelScope.launch {
                when (updateNotificationSetting(category, isEnabled)) {
                    is AppResult.Success -> {
                        loadSettings()
                    }

                    is AppResult.Failure -> {
                        mutableUiState.value = currentState
                        mutableEffects.emit(NotificationSettingsEffect.UpdateFailed)
                    }
                }
            }
        }

        private fun loadSettings() {
            mutableUiState.value = NotificationSettingsUiState.Loading
            viewModelScope.launch {
                mutableUiState.value = getNotificationSettings().toUiState()
            }
        }
    }

sealed interface NotificationSettingsEffect {
    data object UpdateFailed : NotificationSettingsEffect
}

private fun AppResult<List<NotificationSetting>>.toUiState(): NotificationSettingsUiState =
    when (this) {
        is AppResult.Success -> {
            if (value.isEmpty()) {
                NotificationSettingsUiState.Empty
            } else {
                NotificationSettingsUiState.Content(
                    value.map { setting ->
                        NotificationSettingUiModel(
                            category = setting.category,
                            label = setting.label,
                            isEnabled = setting.isEnabled,
                        )
                    },
                )
            }
        }

        is AppResult.Failure -> {
            NotificationSettingsUiState.Failed
        }
    }
