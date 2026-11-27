package com.nativelap.ecoguard.feature.notification.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationInbox
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationSetting
import com.nativelap.ecoguard.feature.notification.module.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase
    @Inject
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend operator fun invoke(limit: Int = DEFAULT_NOTIFICATION_LIMIT): AppResult<NotificationInbox> =
            repository.getNotifications(limit)
    }

class MarkNotificationReadUseCase
    @Inject
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend operator fun invoke(notificationId: Long): AppResult<Unit> = repository.markRead(notificationId)
    }

class MarkAllNotificationsReadUseCase
    @Inject
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend operator fun invoke(): AppResult<Unit> = repository.markAllRead()
    }

class GetNotificationSettingsUseCase
    @Inject
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<NotificationSetting>> = repository.getSettings()
    }

class UpdateNotificationSettingUseCase
    @Inject
    constructor(
        private val repository: NotificationRepository,
    ) {
        suspend operator fun invoke(
            category: String,
            isEnabled: Boolean,
        ): AppResult<Unit> = repository.updateSetting(category, isEnabled)
    }

private const val DEFAULT_NOTIFICATION_LIMIT = 50
