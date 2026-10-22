package com.nativelap.ecoguard.feature.notice.module.data.remote

import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeDetailResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeListItemResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.UpdateNoticeRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NoticeApiService {
    @GET("api/v1/notices")
    suspend fun getNotices(): Response<List<NoticeListItemResponseDto>>

    @POST("api/v1/notices")
    suspend fun createNotice(
        @Body request: CreateNoticeRequestDto,
    ): Response<CreateNoticeResponseDto>

    @GET("api/v1/notices/{noticeId}")
    suspend fun getNoticeDetail(
        @Path("noticeId") noticeId: Long,
    ): Response<NoticeDetailResponseDto>

    @DELETE("api/v1/notices/{noticeId}")
    suspend fun deleteNotice(
        @Path("noticeId") noticeId: Long,
    ): Response<Unit>

    @PATCH("api/v1/notices/{noticeId}")
    suspend fun updateNotice(
        @Path("noticeId") noticeId: Long,
        @Body request: UpdateNoticeRequestDto,
    ): Response<Unit>
}
