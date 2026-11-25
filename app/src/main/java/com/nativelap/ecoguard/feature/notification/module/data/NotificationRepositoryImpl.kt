package com.nativelap.ecoguard.feature.notification.module.data

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSource
import com.nativelap.ecoguard.feature.notification.module.domain.InboxNotification
import com.nativelap.ecoguard.feature.notification.module.domain.NotificationInbox
import com.nativelap.ecoguard.feature.notification.module.domain.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl
    @Inject
    constructor(
        private val remote: NotificationRemoteDataSource,
    ) : NotificationRepository {
        override suspend fun getInbox(): NetworkResult<NotificationInbox> =
            when (val response = remote.getInbox()) {
                is NetworkResult.Failure -> {
                    response
                }

                is NetworkResult.Success -> {
                    val inbox = response.value
                    NetworkResult.Success(
                        NotificationInbox(
                            unreadCount = inbox.unreadCount,
                            notifications =
                                inbox.notifications.map { notification ->
                                    InboxNotification(
                                        id = notification.id,
                                        title = notification.title,
                                        body = notification.body,
                                        isRead = notification.isRead,
                                        createdAt = notification.createdAt,
                                    )
                                },
                        ),
                    )
                }
            }

        override suspend fun markRead(id: Long) = remote.markRead(id)

        override suspend fun markAllRead() = remote.markAllRead()
    }
