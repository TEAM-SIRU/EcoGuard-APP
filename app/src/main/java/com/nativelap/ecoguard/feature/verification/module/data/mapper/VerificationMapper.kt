package com.nativelap.ecoguard.feature.verification.module.data.mapper

import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewDecisionRequestDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewItemResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.MyVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ReviewResultResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.SubmitVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.TodayVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewDecision
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewItem
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.ReviewResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.SubmitVerificationResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.TodayVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatus
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatusValue
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationUnavailableReason
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationUnavailableReasonValue

fun SubmitVerificationResponseDto.toDomain() =
    SubmitVerificationResult(
        verificationId = verificationId,
        status = status.toVerificationStatusValue(),
        submittedAt = submittedAt,
    )

fun ManualReviewItemResponseDto.toDomain() =
    ManualReviewItem(
        verificationId = verificationId,
        studentName = studentName,
        areaName = areaName,
        photoUrl = photoUrl,
        submittedAt = submittedAt,
        reason = reason,
        failReasons = failReasons,
    )

fun ReviewResultResponseDto.toDomain() =
    ReviewResult(
        status = status.toVerificationStatusValue(),
        failReasons = failReasons,
    )

fun TodayVerificationResponseDto.toDomain() =
    TodayVerification(
        serverTime = serverTime,
        areaId = areaId,
        areaName = areaName,
        cleanTime = cleanTime,
        startTime = startTime,
        endTime = endTime,
        canSubmit = canSubmit,
        unavailableReason = unavailableReason.toUnavailableReasonValue(),
        submitted = submitted,
        verificationId = verificationId,
        status = status.toVerificationStatusValue(),
        submittedAt = submittedAt,
    )

fun MyVerificationResponseDto.toDomain() =
    MyVerification(
        verificationId = verificationId,
        photoUrl = photoUrl,
        date = date,
        submittedAt = submittedAt,
        areaName = areaName,
        reviewStatus = reviewStatus.toVerificationStatusValue(),
        failReasons = failReasons,
    )

fun ManualReviewDecision.toRequestDto(reason: String?) =
    ManualReviewDecisionRequestDto(
        decision = name,
        reason = reason,
    )

private fun String?.toVerificationStatusValue(): VerificationStatusValue? {
    if (this == null) return null
    val knownStatus = VerificationStatus.entries.firstOrNull { it.name == this } ?: VerificationStatus.UNKNOWN
    return VerificationStatusValue(value = knownStatus, rawValue = this)
}

private fun String?.toUnavailableReasonValue(): VerificationUnavailableReasonValue? {
    if (this == null) return null
    val knownReason = VerificationUnavailableReason.entries.firstOrNull { it.name == this }
    return VerificationUnavailableReasonValue(
        value = knownReason ?: VerificationUnavailableReason.UNKNOWN,
        rawValue = this,
    )
}
