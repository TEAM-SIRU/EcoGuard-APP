package com.nativelap.ecoguard.feature.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationInbox
import com.nativelap.ecoguard.feature.notification.module.domain.usecase.GetNotificationsUseCase
import com.nativelap.ecoguard.feature.notification.module.domain.usecase.MarkAllNotificationsReadUseCase
import com.nativelap.ecoguard.feature.notification.module.domain.usecase.MarkNotificationReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        private val getNotifications: GetNotificationsUseCase,
        private val markNotificationRead: MarkNotificationReadUseCase,
        private val markAllNotificationsRead: MarkAllNotificationsReadUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
        val uiState = mutableUiState.asStateFlow()
        private val mutableEffects = MutableSharedFlow<NotificationEffect>()
        val effects = mutableEffects.asSharedFlow()
        private val mutationQueue =
            NotificationMutationQueue { isPending ->
                mutableUiState.update { currentState ->
                    if (currentState is NotificationUiState.Content) {
                        currentState.copy(isUpdatingReadState = isPending)
                    } else {
                        currentState
                    }
                }
            }

        init {
            loadNotifications()
        }

        fun retry() {
            loadNotifications()
        }

        fun markRead(notificationId: Long) {
            val notification =
                (mutableUiState.value as? NotificationUiState.Content)
                    ?.notifications
                    ?.firstOrNull { it.notificationId == notificationId }
            if (notification == null || notification.isRead) return

            viewModelScope.launch {
                mutationQueue.enqueue {
                    when (markNotificationRead(notificationId)) {
                        is AppResult.Success -> refreshNotifications()
                        is AppResult.Failure -> mutableEffects.emit(NotificationEffect.UpdateFailed)
                    }
                }
            }
        }

        fun markAllRead() {
            viewModelScope.launch {
                mutationQueue.enqueue {
                    when (markAllNotificationsRead()) {
                        is AppResult.Success -> refreshNotifications()
                        is AppResult.Failure -> mutableEffects.emit(NotificationEffect.UpdateFailed)
                    }
                }
            }
        }

        private fun loadNotifications() {
            mutableUiState.value = NotificationUiState.Loading
            viewModelScope.launch {
                mutableUiState.value = getNotifications().toUiState()
            }
        }

        private suspend fun refreshNotifications() {
            mutableUiState.value = getNotifications().toUiState(isUpdatingReadState = true)
        }
    }

sealed interface NotificationEffect {
    data object UpdateFailed : NotificationEffect
}

private fun AppResult<NotificationInbox>.toUiState(isUpdatingReadState: Boolean = false): NotificationUiState =
    when (this) {
        is AppResult.Success -> {
            if (value.notifications.isEmpty()) {
                NotificationUiState.Empty
            } else {
                NotificationUiState.Content(
                    unreadCount = value.unreadCount,
                    isUpdatingReadState = isUpdatingReadState,
                    notifications =
                        value.notifications.map { notification ->
                            NotificationUiModel(
                                notificationId = notification.id,
                                type = notification.type,
                                title = notification.title,
                                body = notification.body,
                                sentDate = notification.createdAt.toNotificationDate(),
                                isRead = notification.isRead,
                            )
                        },
                )
            }
        }

        is AppResult.Failure -> {
            NotificationUiState.Failed
        }
    }

private fun String.toNotificationDate(): String =
    runCatching {
        OffsetDateTime.parse(this).format(NOTIFICATION_DATE_FORMATTER)
    }.recoverCatching {
        LocalDateTime.parse(this).format(NOTIFICATION_DATE_FORMATTER)
    }.getOrDefault(this)

private val NOTIFICATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("M월 d일 HH:mm", Locale.KOREAN)
