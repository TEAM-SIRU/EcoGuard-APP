package com.nativelap.ecoguard.feature.notice.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateNoticeResponseDto(
    @SerialName("noticeId") val noticeId: Long? = null,
)

@Serializable
data class NoticeListItemResponseDto(
    @SerialName("noticeId") val noticeId: Long,
    @SerialName("title") val title: String,
    @SerialName("preview") val preview: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("read") val isRead: Boolean,
)

@Serializable
data class NoticeDetailResponseDto(
    @SerialName("noticeId") val noticeId: Long,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("previousNoticeId") val previousNoticeId: Long? = null,
    @SerialName("nextNoticeId") val nextNoticeId: Long? = null,
)
