package com.nativelap.ecoguard.feature.notification.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class NotificationUiModel(
    val notificationId: Long,
    val title: String,
    val sentDate: String,
    val target: NotificationTarget,
    val isRead: Boolean = false,
)
