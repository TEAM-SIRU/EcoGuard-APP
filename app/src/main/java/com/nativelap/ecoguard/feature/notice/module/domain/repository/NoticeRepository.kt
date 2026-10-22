package com.nativelap.ecoguard.feature.notice.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.model.CreatedNotice
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeDetail
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem

interface NoticeRepository {
    suspend fun getNotices(): AppResult<List<NoticeListItem>>

    suspend fun createNotice(
        title: String?,
        content: String?,
    ): AppResult<CreatedNotice>

    suspend fun getNoticeDetail(noticeId: Long): AppResult<NoticeDetail>

    suspend fun deleteNotice(noticeId: Long): AppResult<Unit>

    suspend fun updateNotice(
        noticeId: Long,
        title: String?,
        content: String?,
    ): AppResult<Unit>
}
