package com.nativelap.ecoguard.feature.notification.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class NotificationListResponseDto(
    @SerialName("unreadCount") val unreadCount: Int,
    @SerialName("notifications") val notifications: List<NotificationResponseDto>,
)

@Serializable
data class NotificationResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("refId") val refId: JsonElement? = null,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("read") val isRead: Boolean,
)

@Serializable
data class NotificationSettingResponseDto(
    @SerialName("category") val category: String,
    @SerialName("label") val label: String,
    @SerialName("enabled") val isEnabled: Boolean,
)

@Serializable
data class UpdateNotificationSettingRequestDto(
    @SerialName("category") val category: String,
    @SerialName("enabled") val isEnabled: Boolean,
)
