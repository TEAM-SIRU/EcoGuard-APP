package com.nativelap.ecoguard.feature.recruitment.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecruitmentRequestDto(
    @SerialName("semester") val semester: String,
    @SerialName("grade") val grade: Int,
    @SerialName("classNo") val classNo: Int,
    @SerialName("maxCount") val maxCount: Int,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String,
    @SerialName("activityStartTime") val activityStartTime: String? = null,
    @SerialName("activityEndTime") val activityEndTime: String? = null,
)

@Serializable
data class CreateRecruitmentResponseDto(
    @SerialName("recruitmentId") val recruitmentId: Long? = null,
)

@Serializable
data class ApplyRequestDto(
    @SerialName("motivation") val motivation: String,
)

@Serializable
data class ApplyResponseDto(
    @SerialName("applicationId") val applicationId: Long? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("order") val order: Long? = null,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("appliedAt") val appliedAt: String? = null,
)

@Serializable
data class UpdateRecruitmentRequestDto(
    @SerialName("maxCount") val maxCount: Int? = null,
    @SerialName("startDate") val startDate: String? = null,
    @SerialName("endDate") val endDate: String? = null,
    @SerialName("activityStartTime") val activityStartTime: String? = null,
    @SerialName("activityEndTime") val activityEndTime: String? = null,
)

@Serializable
data class RecruitmentPeriodDto(
    @SerialName("start") val start: String? = null,
    @SerialName("end") val end: String? = null,
)

@Serializable
data class ActivityTimeDto(
    @SerialName("start") val start: String? = null,
    @SerialName("end") val end: String? = null,
)

@Serializable
data class RecruitmentSummaryResponseDto(
    @SerialName("recruitmentId") val recruitmentId: Long? = null,
    @SerialName("semester") val semester: String? = null,
    @SerialName("grade") val grade: Int? = null,
    @SerialName("classNo") val classNo: Int? = null,
    @SerialName("period") val period: RecruitmentPeriodDto? = null,
    @SerialName("activityTime") val activityTime: ActivityTimeDto? = null,
    @SerialName("periodStatus") val periodStatus: String? = null,
    @SerialName("maxCount") val maxCount: Int? = null,
    @SerialName("applicantCount") val applicantCount: Long? = null,
    @SerialName("full") val full: Boolean? = null,
)

@Serializable
data class ApplicantResponseDto(
    @SerialName("applicationId") val applicationId: Long? = null,
    @SerialName("order") val order: Long? = null,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("motivation") val motivation: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("appliedAt") val appliedAt: String? = null,
)

@Serializable
data class CurrentRecruitmentResponseDto(
    @SerialName("recruitmentId") val recruitmentId: Long? = null,
    @SerialName("semester") val semester: String? = null,
    @SerialName("grade") val grade: Int? = null,
    @SerialName("classNo") val classNo: Int? = null,
    @SerialName("period") val period: RecruitmentPeriodDto? = null,
    @SerialName("activityTime") val activityTime: ActivityTimeDto? = null,
    @SerialName("periodStatus") val periodStatus: String? = null,
    @SerialName("maxCount") val maxCount: Int? = null,
    @SerialName("currentApplicants") val currentApplicants: Long? = null,
    @SerialName("alreadyApplied") val alreadyApplied: Boolean? = null,
    @SerialName("full") val full: Boolean? = null,
)

@Serializable
data class ApplicationStatusResponseDto(
    @SerialName("recruitmentId") val recruitmentId: Long? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("order") val order: Long? = null,
    @SerialName("appliedAt") val appliedAt: String? = null,
    @SerialName("waitingForAssignment") val waitingForAssignment: Boolean? = null,
)
