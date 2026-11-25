package com.nativelap.ecoguard.feature.notification.module.domain

import com.nativelap.ecoguard.core.network.NetworkResult

data class NotificationInbox(
    val unreadCount: Long,
    val notifications: List<InboxNotification>,
)

data class InboxNotification(
    val id: Long,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: String,
)

interface NotificationRepository {
    suspend fun getInbox(): NetworkResult<NotificationInbox>

    suspend fun markRead(id: Long): NetworkResult<Unit>

    suspend fun markAllRead(): NetworkResult<Unit>
}
