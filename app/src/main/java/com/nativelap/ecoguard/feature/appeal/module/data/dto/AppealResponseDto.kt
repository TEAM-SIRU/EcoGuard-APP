package com.nativelap.ecoguard.feature.appeal.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppealResponseDto(
    @SerialName("appealId") val appealId: Long? = null,
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("round") val round: Int? = null,
    @SerialName("studentName") val studentName: String? = null,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("areaName") val areaName: String? = null,
    @SerialName("verificationDate") val verificationDate: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("failReasons") val failReasons: List<String>? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("photoUrls") val photoUrls: List<String>? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("replyTitle") val replyTitle: String? = null,
    @SerialName("reply") val reply: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
)

@Serializable
data class MyAppealResponseDto(
    @SerialName("appealId") val appealId: Long? = null,
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("round") val round: Int? = null,
    @SerialName("areaName") val areaName: String? = null,
    @SerialName("verificationDate") val verificationDate: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("photoUrls") val photoUrls: List<String>? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("awardedMinutes") val awardedMinutes: Int? = null,
    @SerialName("replyTitle") val replyTitle: String? = null,
    @SerialName("reply") val reply: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
)
