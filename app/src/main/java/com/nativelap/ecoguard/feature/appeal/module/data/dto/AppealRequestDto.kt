package com.nativelap.ecoguard.feature.appeal.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAppealRequestDto(
    @SerialName("content") val content: String,
)

@Serializable
data class AppealDecisionRequestDto(
    @SerialName("decision") val decision: String? = null,
    @SerialName("reply") val reply: String? = null,
    @SerialName("replyTitle") val replyTitle: String? = null,
)
