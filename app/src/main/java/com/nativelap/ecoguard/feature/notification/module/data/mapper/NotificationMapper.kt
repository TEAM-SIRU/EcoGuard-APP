package com.nativelap.ecoguard.feature.notification.module.data.mapper

import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationListResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationSettingResponseDto
import com.nativelap.ecoguard.feature.notification.module.domain.model.Notification
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationInbox
import com.nativelap.ecoguard.feature.notification.module.domain.model.NotificationSetting
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

fun NotificationListResponseDto.toDomain(): NotificationInbox =
    NotificationInbox(
        unreadCount = unreadCount,
        notifications = notifications.map(NotificationResponseDto::toDomain),
    )

fun NotificationResponseDto.toDomain(): Notification =
    Notification(
        id = id,
        type = type,
        title = title,
        body = body,
        refId =
            when (val referenceId = refId) {
                null, JsonNull -> null
                is JsonPrimitive -> referenceId.content
                else -> referenceId.toString()
            },
        createdAt = createdAt,
        isRead = isRead,
    )

fun NotificationSettingResponseDto.toDomain(): NotificationSetting =
    NotificationSetting(
        category = category,
        label = label,
        isEnabled = isEnabled,
    )
