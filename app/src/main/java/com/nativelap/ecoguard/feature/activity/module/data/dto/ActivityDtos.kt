package com.nativelap.ecoguard.feature.activity.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityRecordDto(
    @SerialName("date") val date: String? = null,
    @SerialName("area") val area: String? = null,
    @SerialName("result") val result: String? = null,
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("minutes") val minutes: Int? = null,
)

@Serializable
data class ActivitySummaryDto(
    @SerialName("completedDays") val completedDays: Int? = null,
    @SerialName("requiredDays") val requiredDays: Int? = null,
    @SerialName("approvedCount") val approvedCount: Int? = null,
    @SerialName("rejectedCount") val rejectedCount: Int? = null,
    @SerialName("notSubmittedCount") val notSubmittedCount: Int? = null,
)

@Serializable
data class MyActivityResponseDto(
    @SerialName("totalMinutes") val totalMinutes: Long? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("month") val month: Int? = null,
    @SerialName("monthlyMinutes") val monthlyMinutes: Int? = null,
    @SerialName("summary") val summary: ActivitySummaryDto? = null,
    @SerialName("records") val records: List<ActivityRecordDto>? = null,
)

@Serializable
data class StudentActivityResponseDto(
    @SerialName("studentId") val studentId: Long? = null,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("area") val area: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("attendanceDays") val attendanceDays: Long? = null,
    @SerialName("totalMinutes") val totalMinutes: Long? = null,
)

@Serializable
data class WeeklyDayDto(
    @SerialName("date") val date: String? = null,
    @SerialName("result") val result: String? = null,
)

@Serializable
data class WeeklyActivityResponseDto(
    @SerialName("weekStart") val weekStart: String? = null,
    @SerialName("weekEnd") val weekEnd: String? = null,
    @SerialName("completedDays") val completedDays: Int? = null,
    @SerialName("requiredDays") val requiredDays: Int? = null,
    @SerialName("days") val days: List<WeeklyDayDto>? = null,
)
