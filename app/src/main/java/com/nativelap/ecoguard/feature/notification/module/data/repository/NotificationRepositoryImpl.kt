package com.nativelap.ecoguard.feature.notification.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.notification.module.data.dto.UpdateNotificationSettingRequestDto
import com.nativelap.ecoguard.feature.notification.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSource
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationInbox
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationSetting
import com.nativelap.ecoguard.feature.notification.module.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: NotificationRemoteDataSource,
    ) : NotificationRepository {
        override suspend fun getNotifications(limit: Int): AppResult<NotificationInbox> =
            when (val response = remoteDataSource.getNotifications(limit)) {
                is NetworkResult.Success -> AppResult.Success(response.value.toDomain())
                is NetworkResult.Failure -> response.toAppResult()
            }

        override suspend fun markRead(notificationId: Long): AppResult<Unit> =
            remoteDataSource.markRead(notificationId).toUnitAppResult()

        override suspend fun markAllRead(): AppResult<Unit> = remoteDataSource.markAllRead().toUnitAppResult()

        override suspend fun getSettings(): AppResult<List<NotificationSetting>> =
            when (val response = remoteDataSource.getSettings()) {
                is NetworkResult.Success -> AppResult.Success(response.value.map { it.toDomain() })
                is NetworkResult.Failure -> response.toAppResult()
            }

        override suspend fun updateSetting(
            category: String,
            isEnabled: Boolean,
        ): AppResult<Unit> =
            remoteDataSource
                .updateSetting(UpdateNotificationSettingRequestDto(category, isEnabled))
                .toUnitAppResult()
    }

private fun NetworkResult<Unit>.toUnitAppResult(): AppResult<Unit> =
    when (this) {
        is NetworkResult.Success -> AppResult.Success(Unit)
        is NetworkResult.Failure -> toAppResult()
    }
