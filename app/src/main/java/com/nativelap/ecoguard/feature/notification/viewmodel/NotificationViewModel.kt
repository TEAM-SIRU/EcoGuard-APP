package com.nativelap.ecoguard.feature.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notification.module.domain.NotificationInboxUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        private val inboxUseCase: NotificationInboxUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
        val uiState = _uiState.asStateFlow()
        private var request: Job? = null

        fun load() {
            updateInbox()
        }

        fun markRead(id: Long) {
            updateInbox { inboxUseCase.markRead(id) }
        }

        fun markAllRead() {
            updateInbox { inboxUseCase.markAllRead() }
        }

        private fun updateInbox(mutation: (suspend () -> NetworkResult<Unit>)? = null) {
            if (request?.isActive == true) return
            request =
                viewModelScope.launch {
                    val previous = _uiState.value as? NotificationUiState.Content
                    _uiState.value =
                        previous?.copy(isUpdating = true, updateFailed = false) ?: NotificationUiState.Loading
                    if (mutation?.invoke() is NetworkResult.Failure) {
                        _uiState.value = previous?.copy(updateFailed = true) ?: NotificationUiState.Failed
                        return@launch
                    }
                    _uiState.value =
                        when (val response = inboxUseCase.load()) {
                            is NetworkResult.Failure -> {
                                previous?.copy(updateFailed = true)
                                    ?: NotificationUiState.Failed
                            }

                            is NetworkResult.Success -> {
                                val inbox = response.value
                                if (inbox.notifications.isEmpty()) {
                                    NotificationUiState.Empty
                                } else {
                                    NotificationUiState.Content(
                                        notifications =
                                            inbox.notifications.map { notification ->
                                                NotificationUiModel(
                                                    notificationId = notification.id,
                                                    title = notification.title,
                                                    sentDate = notification.createdAt.take(10),
                                                    target = null,
                                                    isRead = notification.isRead,
                                                    body = notification.body,
                                                )
                                            },
                                        unreadCount = inbox.unreadCount,
                                    )
                                }
                            }
                        }
                }
        }
    }
