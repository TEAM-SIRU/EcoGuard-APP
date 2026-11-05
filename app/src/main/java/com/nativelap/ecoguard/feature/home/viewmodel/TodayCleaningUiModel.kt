package com.nativelap.ecoguard.feature.home.viewmodel

import androidx.compose.runtime.Immutable
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationUnavailableReason

@Immutable
data class TodayCleaningUiModel(
    val areaId: Long,
    val cleaningStartTime: String,
    val cleaningEndTime: String,
    val assignedAreaName: String,
    val status: TodayCleaningStatus,
    val verificationId: Long? = null,
)

@Immutable
sealed interface TodayCleaningStatus {
    data class NotSubmitted(
        val timeUntilDeadline: String,
    ) : TodayCleaningStatus

    data class OutsideVerificationTime(
        val availableFromTime: String,
        val reason: VerificationUnavailableReason = VerificationUnavailableReason.BEFORE_START,
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
