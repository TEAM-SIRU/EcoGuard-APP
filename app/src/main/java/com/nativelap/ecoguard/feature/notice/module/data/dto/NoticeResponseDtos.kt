package com.nativelap.ecoguard.feature.notice.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateNoticeResponseDto(
    @SerialName("noticeId") val noticeId: Long? = null,
)

@Serializable
data class NoticeListItemResponseDto(
    @SerialName("noticeId") val noticeId: Long? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("preview") val preview: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("read") val isRead: Boolean? = null,
)

@Serializable
data class NoticeDetailResponseDto(
    @SerialName("noticeId") val noticeId: Long? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("previousNoticeId") val previousNoticeId: Long? = null,
    @SerialName("nextNoticeId") val nextNoticeId: Long? = null,
)
