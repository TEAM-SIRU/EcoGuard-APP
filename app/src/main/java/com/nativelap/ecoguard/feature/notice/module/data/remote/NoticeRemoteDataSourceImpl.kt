package com.nativelap.ecoguard.feature.notice.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeDetailResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeListItemResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.UpdateNoticeRequestDto
import javax.inject.Inject

class NoticeRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: NoticeApiService,
        private val apiExecutor: ApiExecutor,
    ) : NoticeRemoteDataSource {
        override suspend fun getNotices(): NetworkResult<List<NoticeListItemResponseDto>> =
            apiExecutor.execute { apiService.getNotices() }

        override suspend fun createNotice(request: CreateNoticeRequestDto): NetworkResult<CreateNoticeResponseDto> =
            apiExecutor.execute {
                apiService.createNotice(request)
            }

        override suspend fun getNoticeDetail(noticeId: Long): NetworkResult<NoticeDetailResponseDto> =
            apiExecutor.execute { apiService.getNoticeDetail(noticeId) }

        override suspend fun deleteNotice(noticeId: Long): NetworkResult<Unit> =
            apiExecutor.executeNoContent { apiService.deleteNotice(noticeId) }

        override suspend fun updateNotice(
            noticeId: Long,
            request: UpdateNoticeRequestDto,
        ): NetworkResult<Unit> =
            apiExecutor.executeNoContent {
                apiService.updateNotice(noticeId, request)
            }
    }
