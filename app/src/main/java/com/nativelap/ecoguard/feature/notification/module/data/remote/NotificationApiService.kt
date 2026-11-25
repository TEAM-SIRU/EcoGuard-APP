package com.nativelap.ecoguard.feature.notification.module.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationApiService {
    @GET("api/v1/notifications")
    suspend fun getInbox(): Response<NotificationInboxDto>

    @PATCH("api/v1/notifications/{notificationId}/read")
    suspend fun markRead(
        @Path("notificationId") notificationId: Long,
    ): Response<Unit>

    @PATCH("api/v1/notifications/read-all")
    suspend fun markAllRead(): Response<Unit>
}

@Serializable
data class NotificationInboxDto(
    val unreadCount: Long,
    val notifications: List<NotificationDto>,
)

@Serializable
data class NotificationDto(
    val id: Long,
    val title: String,
    val body: String,
    @SerialName("isRead") val isRead: Boolean,
    val createdAt: String,
)
