package com.nativelap.ecoguard.feature.notification.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationListResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationSettingResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.UpdateNotificationSettingRequestDto
import javax.inject.Inject

class NotificationRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: NotificationApiService,
        private val apiExecutor: ApiExecutor,
    ) : NotificationRemoteDataSource {
        override suspend fun getNotifications(limit: Int): NetworkResult<NotificationListResponseDto> =
            apiExecutor.execute { apiService.getNotifications(limit) }

        override suspend fun markRead(notificationId: Long): NetworkResult<Unit> =
            apiExecutor.executeNoContent { apiService.markRead(notificationId) }

        override suspend fun markAllRead(): NetworkResult<Unit> =
            apiExecutor.executeNoContent { apiService.markAllRead() }

        override suspend fun getSettings(): NetworkResult<List<NotificationSettingResponseDto>> =
            apiExecutor.execute { apiService.getSettings() }

        override suspend fun updateSetting(request: UpdateNotificationSettingRequestDto): NetworkResult<Unit> =
            apiExecutor.executeNoContent { apiService.updateSetting(request) }
    }
