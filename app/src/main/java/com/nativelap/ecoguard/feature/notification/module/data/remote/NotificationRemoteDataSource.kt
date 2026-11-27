package com.nativelap.ecoguard.feature.notification.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationListResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationSettingResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.UpdateNotificationSettingRequestDto

interface NotificationRemoteDataSource {
    suspend fun getNotifications(limit: Int): NetworkResult<NotificationListResponseDto>

    suspend fun markRead(notificationId: Long): NetworkResult<Unit>

    suspend fun markAllRead(): NetworkResult<Unit>

    suspend fun getSettings(): NetworkResult<List<NotificationSettingResponseDto>>

    suspend fun updateSetting(request: UpdateNotificationSettingRequestDto): NetworkResult<Unit>
}
