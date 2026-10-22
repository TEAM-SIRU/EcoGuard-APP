package com.nativelap.ecoguard.feature.notice.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateNoticeRequestDto(
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
)

@Serializable
data class UpdateNoticeRequestDto(
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
)
