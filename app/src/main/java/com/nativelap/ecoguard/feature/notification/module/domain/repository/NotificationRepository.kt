package com.nativelap.ecoguard.feature.notification.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationInbox
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationSetting

interface NotificationRepository {
    suspend fun getNotifications(limit: Int): AppResult<NotificationInbox>

    suspend fun markRead(notificationId: Long): AppResult<Unit>

    suspend fun markAllRead(): AppResult<Unit>

    suspend fun getSettings(): AppResult<List<NotificationSetting>>

    suspend fun updateSetting(
        category: String,
        isEnabled: Boolean,
    ): AppResult<Unit>
}
