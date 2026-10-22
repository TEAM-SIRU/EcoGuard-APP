package com.nativelap.ecoguard.feature.verification.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewDecision
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewItem
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.ReviewResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.SubmitVerificationResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.TodayVerification
import java.io.File

interface VerificationRepository {
    suspend fun submitVerification(
        photo: File,
        areaId: Long?,
        idempotencyKey: String?,
        submitStartedAt: String?,
    ): AppResult<SubmitVerificationResult>

    suspend fun getManualReviewQueue(): AppResult<List<ManualReviewItem>>

    suspend fun getReviewResult(verificationId: Long): AppResult<ReviewResult>

    suspend fun decideReview(
        verificationId: Long,
        decision: ManualReviewDecision,
        reason: String?,
    ): AppResult<Unit>

    suspend fun getTodayVerification(): AppResult<TodayVerification>

    suspend fun getMyVerifications(): AppResult<List<MyVerification>>
}
