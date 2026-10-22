package com.nativelap.ecoguard.feature.notice.module.data.mapper

import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeDetailResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeListItemResponseDto
import com.nativelap.ecoguard.feature.notice.module.domain.model.CreatedNotice
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeDetail
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem

fun NoticeListItemResponseDto.toDomain(): NoticeListItem =
    NoticeListItem(
        noticeId = noticeId,
        title = title,
        preview = preview,
        createdAt = createdAt,
        isRead = isRead,
    )

fun NoticeDetailResponseDto.toDomain(): NoticeDetail =
    NoticeDetail(
        noticeId = noticeId,
        title = title,
        content = content,
        createdAt = createdAt,
        previousNoticeId = previousNoticeId,
        nextNoticeId = nextNoticeId,
    )

fun CreateNoticeResponseDto.toDomain(): CreatedNotice =
    CreatedNotice(
        noticeId = noticeId,
    )
