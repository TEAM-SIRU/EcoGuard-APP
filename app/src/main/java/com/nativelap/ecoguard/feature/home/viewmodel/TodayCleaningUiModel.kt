package com.nativelap.ecoguard.feature.home.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class TodayCleaningUiModel(
    val cleaningStartTime: String,
    val cleaningEndTime: String,
    val assignedAreaName: String,
    val status: TodayCleaningStatus,
)

@Immutable
sealed interface TodayCleaningStatus {
    data class NotSubmitted(
        val timeUntilDeadline: String,
    ) : TodayCleaningStatus

    data class OutsideVerificationTime(
        val availableFromTime: String,
    ) : TodayCleaningStatus

    data class AiReviewing(
        val submittedTime: String,
    ) : TodayCleaningStatus

    data class TeacherReviewing(
        val submittedTime: String,
    ) : TodayCleaningStatus

    data class Approved(
        val earnedMinutes: Int,
    ) : TodayCleaningStatus

    data class Rejected(
        val rejectionReason: String,
    ) : TodayCleaningStatus
}
