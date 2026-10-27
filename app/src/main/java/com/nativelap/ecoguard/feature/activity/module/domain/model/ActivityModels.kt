package com.nativelap.ecoguard.feature.activity.module.domain.model

enum class ActivityResultStatus {
    APPROVED,
    REJECTED,
    PROCESSING,
    MANUAL_REVIEW,
    NOT_SUBMITTED,
    UPCOMING,
    UNKNOWN,
}

data class ActivityRecord(
    val date: String,
    val area: String?,
    val result: ActivityResultStatus,
    val verificationId: Long?,
    val photoUrl: String?,
    val minutes: Int,
)

data class ActivitySummary(
    val completedDays: Int,
    val requiredDays: Int,
    val approvedCount: Int,
    val rejectedCount: Int,
    val notSubmittedCount: Int,
)

data class MyActivity(
    val totalMinutes: Long,
    val year: Int,
    val month: Int,
    val monthlyMinutes: Int,
    val summary: ActivitySummary,
    val records: List<ActivityRecord>,
)

data class StudentActivity(
    val studentId: Long,
    val studentNumber: String?,
    val name: String,
    val area: String?,
    val photoUrl: String?,
    val attendanceDays: Long,
    val totalMinutes: Long,
)

data class WeeklyDay(
    val date: String,
    val result: ActivityResultStatus,
)

data class WeeklyActivity(
    val weekStart: String,
    val weekEnd: String,
    val completedDays: Int,
    val requiredDays: Int,
    val days: List<WeeklyDay>,
)
