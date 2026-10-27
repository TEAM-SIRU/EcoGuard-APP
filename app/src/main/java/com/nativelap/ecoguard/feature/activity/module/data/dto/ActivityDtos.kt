package com.nativelap.ecoguard.feature.activity.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityRecordDto(
    @SerialName("date") val date: String,
    @SerialName("area") val area: String? = null,
    @SerialName("result") val result: String,
    @SerialName("verificationId") val verificationId: Long? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("minutes") val minutes: Int,
)

@Serializable
data class ActivitySummaryDto(
    @SerialName("completedDays") val completedDays: Int,
    @SerialName("requiredDays") val requiredDays: Int,
    @SerialName("approvedCount") val approvedCount: Int,
    @SerialName("rejectedCount") val rejectedCount: Int,
    @SerialName("notSubmittedCount") val notSubmittedCount: Int,
)

@Serializable
data class MyActivityResponseDto(
    @SerialName("totalMinutes") val totalMinutes: Long,
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("monthlyMinutes") val monthlyMinutes: Int,
    @SerialName("summary") val summary: ActivitySummaryDto,
    @SerialName("records") val records: List<ActivityRecordDto>,
)

@Serializable
data class StudentActivityResponseDto(
    @SerialName("studentId") val studentId: Long,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("name") val name: String,
    @SerialName("area") val area: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("attendanceDays") val attendanceDays: Long,
    @SerialName("totalMinutes") val totalMinutes: Long,
)

@Serializable
data class WeeklyDayDto(
    @SerialName("date") val date: String,
    @SerialName("result") val result: String,
)

@Serializable
data class WeeklyActivityResponseDto(
    @SerialName("weekStart") val weekStart: String,
    @SerialName("weekEnd") val weekEnd: String,
    @SerialName("completedDays") val completedDays: Int,
    @SerialName("requiredDays") val requiredDays: Int,
    @SerialName("days") val days: List<WeeklyDayDto>,
)
