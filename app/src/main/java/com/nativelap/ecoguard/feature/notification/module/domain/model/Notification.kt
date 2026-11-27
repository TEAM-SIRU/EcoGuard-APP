package com.nativelap.ecoguard.feature.notification.module.domain.model

data class Notification(
    val id: Long,
    val type: String,
    val title: String,
    val body: String,
    val refId: String?,
    val createdAt: String,
    val isRead: Boolean,
)

data class NotificationInbox(
    val unreadCount: Int,
    val notifications: List<Notification>,
)

data class NotificationSetting(
    val category: String,
    val label: String,
    val isEnabled: Boolean,
)
