package com.nativelap.ecoguard.feature.notice.module.domain.model

data class NoticeListItem(
    val noticeId: Long?,
    val title: String?,
    val preview: String?,
    val createdAt: String?,
    val isRead: Boolean?,
)

data class NoticeDetail(
    val noticeId: Long?,
    val title: String?,
    val content: String?,
    val createdAt: String?,
    val previousNoticeId: Long?,
    val nextNoticeId: Long?,
)

data class CreatedNotice(
    val noticeId: Long?,
)
