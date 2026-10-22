package com.nativelap.ecoguard.feature.notice.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeDetailResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeListItemResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.UpdateNoticeRequestDto

interface NoticeRemoteDataSource {
    suspend fun getNotices(): NetworkResult<List<NoticeListItemResponseDto>>

    suspend fun createNotice(request: CreateNoticeRequestDto): NetworkResult<CreateNoticeResponseDto>

    suspend fun getNoticeDetail(noticeId: Long): NetworkResult<NoticeDetailResponseDto>

    suspend fun deleteNotice(noticeId: Long): NetworkResult<Unit>

    suspend fun updateNotice(
        noticeId: Long,
        request: UpdateNoticeRequestDto,
    ): NetworkResult<Unit>
}
