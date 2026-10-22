package com.nativelap.ecoguard.feature.notice.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.UpdateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.notice.module.data.remote.NoticeRemoteDataSource
import com.nativelap.ecoguard.feature.notice.module.domain.model.CreatedNotice
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeDetail
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: NoticeRemoteDataSource,
    ) : NoticeRepository {
        override suspend fun getNotices(): AppResult<List<NoticeListItem>> =
            when (val result = remoteDataSource.getNotices()) {
                is NetworkResult.Success -> AppResult.Success(result.value.map { it.toDomain() })
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun createNotice(
            title: String?,
            content: String?,
        ): AppResult<CreatedNotice> =
            when (
                val result =
                    remoteDataSource.createNotice(
                        CreateNoticeRequestDto(
                            title = title,
                            content = content,
                        ),
                    )
            ) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun getNoticeDetail(noticeId: Long): AppResult<NoticeDetail> =
            when (val result = remoteDataSource.getNoticeDetail(noticeId)) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun deleteNotice(noticeId: Long): AppResult<Unit> =
            when (val result = remoteDataSource.deleteNotice(noticeId)) {
                is NetworkResult.Success -> AppResult.Success(Unit)
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun updateNotice(
            noticeId: Long,
            title: String?,
            content: String?,
        ): AppResult<Unit> =
            when (
                val result =
                    remoteDataSource.updateNotice(
                        noticeId = noticeId,
                        request =
                            UpdateNoticeRequestDto(
                                title = title,
                                content = content,
                            ),
                    )
            ) {
                is NetworkResult.Success -> AppResult.Success(Unit)
                is NetworkResult.Failure -> result.toAppResult()
            }
    }
