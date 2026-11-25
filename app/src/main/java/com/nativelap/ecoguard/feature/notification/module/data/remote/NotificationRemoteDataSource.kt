package com.nativelap.ecoguard.feature.notification.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import javax.inject.Inject

interface NotificationRemoteDataSource {
    suspend fun getInbox(): NetworkResult<NotificationInboxDto>

    suspend fun markRead(id: Long): NetworkResult<Unit>

    suspend fun markAllRead(): NetworkResult<Unit>
}

class NotificationRemoteDataSourceImpl
    @Inject
    constructor(
        private val api: NotificationApiService,
        private val executor: ApiExecutor,
    ) : NotificationRemoteDataSource {
        override suspend fun getInbox() = executor.execute { api.getInbox() }

        override suspend fun markRead(id: Long) = executor.executeNoContent { api.markRead(id) }

        override suspend fun markAllRead() = executor.executeNoContent { api.markAllRead() }
    }
