package com.nativelap.ecoguard.feature.verification.module.domain.model

enum class VerificationStatus {
    PROCESSING,
    APPROVED,
    REJECTED,
    MANUAL_REVIEW,
    UNKNOWN,
}

data class VerificationStatusValue(
    val value: VerificationStatus,
    val rawValue: String,
)

enum class VerificationUnavailableReason {
    ALREADY_SUBMITTED,
    WEEKEND,
    VACATION,
    BEFORE_START,
    AFTER_END,
    UNKNOWN,
}

data class VerificationUnavailableReasonValue(
    val value: VerificationUnavailableReason,
    val rawValue: String?,
)

enum class ManualReviewDecision {
    APPROVED,
    REJECTED,
}

data class SubmitVerificationResult(
    val verificationId: Long?,
    val status: VerificationStatusValue?,
    val submittedAt: String?,
)

data class ManualReviewItem(
    val verificationId: Long?,
    val studentName: String?,
    val areaName: String?,
    val photoUrl: String?,
    val submittedAt: String?,
    val reason: String?,
    val failReasons: List<String>?,
)

data class ReviewResult(
    val status: VerificationStatusValue?,
    val failReasons: List<String>?,
)

data class TodayVerification(
    val serverTime: String?,
    val areaId: Long?,
    val areaName: String?,
    val cleanTime: String?,
    val startTime: String?,
    val endTime: String?,
    val canSubmit: Boolean?,
    val unavailableReason: VerificationUnavailableReasonValue?,
    val submitted: Boolean?,
    val verificationId: Long?,
    val status: VerificationStatusValue?,
    val submittedAt: String?,
)

data class MyVerification(
    val verificationId: Long?,
    val photoUrl: String?,
    val date: String?,
    val submittedAt: String?,
    val areaName: String?,
    val reviewStatus: VerificationStatusValue?,
    val failReasons: List<String>?,
)
