package com.nativelap.ecoguard.feature.verification.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitVerificationResponseDto(
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("submittedAt") val submittedAt: String? = null,
)

@Serializable
data class ManualReviewDecisionRequestDto(
    @SerialName("decision") val decision: String,
    @SerialName("reason") val reason: String? = null,
)

@Serializable
data class ManualReviewItemResponseDto(
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("studentName") val studentName: String? = null,
    @SerialName("areaName") val areaName: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("submittedAt") val submittedAt: String? = null,
    @SerialName("reason") val reason: String? = null,
    @SerialName("failReasons") val failReasons: List<String>? = null,
)

@Serializable
data class ReviewResultResponseDto(
    @SerialName("status") val status: String? = null,
    @SerialName("failReasons") val failReasons: List<String>? = null,
)

@Serializable
data class TodayVerificationResponseDto(
    @SerialName("serverTime") val serverTime: String? = null,
    @SerialName("areaId") val areaId: Long? = null,
    @SerialName("areaName") val areaName: String? = null,
    @SerialName("cleanTime") val cleanTime: String? = null,
    @SerialName("startTime") val startTime: String? = null,
    @SerialName("endTime") val endTime: String? = null,
    @SerialName("canSubmit") val canSubmit: Boolean? = null,
    @SerialName("unavailableReason") val unavailableReason: String? = null,
    @SerialName("submitted") val submitted: Boolean? = null,
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("submittedAt") val submittedAt: String? = null,
)

@Serializable
data class MyVerificationResponseDto(
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("date") val date: String? = null,
    @SerialName("submittedAt") val submittedAt: String? = null,
    @SerialName("areaName") val areaName: String? = null,
    @SerialName("reviewStatus") val reviewStatus: String? = null,
    @SerialName("failReasons") val failReasons: List<String>? = null,
)
