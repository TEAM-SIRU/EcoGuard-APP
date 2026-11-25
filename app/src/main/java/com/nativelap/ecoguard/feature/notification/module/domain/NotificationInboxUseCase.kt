package com.nativelap.ecoguard.feature.notification.module.domain

import javax.inject.Inject

class NotificationInboxUseCase
    @Inject
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend fun load() = repository.getInbox()

        suspend fun markRead(id: Long) = repository.markRead(id)

        suspend fun markAllRead() = repository.markAllRead()
    }
