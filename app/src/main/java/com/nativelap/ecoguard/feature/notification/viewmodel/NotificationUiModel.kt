package com.nativelap.ecoguard.feature.notification.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class NotificationUiModel(
    val notificationId: Long,
    val type: String,
    val title: String,
    val body: String,
    val sentDate: String,
    val isRead: Boolean = false,
)
