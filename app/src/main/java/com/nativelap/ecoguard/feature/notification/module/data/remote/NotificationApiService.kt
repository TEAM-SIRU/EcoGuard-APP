package com.nativelap.ecoguard.feature.notification.module.data.remote

import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationListResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationSettingResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.UpdateNotificationSettingRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("limit") limit: Int,
    ): Response<NotificationListResponseDto>

    @PATCH("api/v1/notifications/{notificationId}/read")
    suspend fun markRead(
        @Path("notificationId") notificationId: Long,
    ): Response<Unit>

    @PATCH("api/v1/notifications/read-all")
    suspend fun markAllRead(): Response<Unit>

    @GET("api/v1/notifications/settings")
    suspend fun getSettings(): Response<List<NotificationSettingResponseDto>>

    @PUT("api/v1/notifications/settings")
    suspend fun updateSetting(
        @Body request: UpdateNotificationSettingRequestDto,
    ): Response<Unit>
}
