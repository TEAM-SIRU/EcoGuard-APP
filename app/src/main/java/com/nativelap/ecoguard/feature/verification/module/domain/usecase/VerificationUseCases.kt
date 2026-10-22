package com.nativelap.ecoguard.feature.verification.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewDecision
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewItem
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.ReviewResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.SubmitVerificationResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.TodayVerification
import com.nativelap.ecoguard.feature.verification.module.domain.repository.VerificationRepository
import java.io.File
import javax.inject.Inject

class SubmitVerificationUseCase
    @Inject
    constructor(
        private val repository: VerificationRepository,
    ) {
        suspend operator fun invoke(
            photo: File,
            areaId: Long? = null,
            idempotencyKey: String? = null,
            submitStartedAt: String? = null,
        ): AppResult<SubmitVerificationResult> =
            repository.submitVerification(photo, areaId, idempotencyKey, submitStartedAt)
    }

class GetManualReviewQueueUseCase
    @Inject
    constructor(
        private val repository: VerificationRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<ManualReviewItem>> = repository.getManualReviewQueue()
    }

class GetReviewResultUseCase
    @Inject
    constructor(
        private val repository: VerificationRepository,
    ) {
        suspend operator fun invoke(verificationId: Long): AppResult<ReviewResult> =
            repository.getReviewResult(verificationId)
    }

class DecideManualReviewUseCase
    @Inject
    constructor(
        private val repository: VerificationRepository,
    ) {
        suspend operator fun invoke(
            verificationId: Long,
            decision: ManualReviewDecision,
            reason: String? = null,
        ): AppResult<Unit> = repository.decideReview(verificationId, decision, reason)
    }

class GetTodayVerificationUseCase
    @Inject
    constructor(
        private val repository: VerificationRepository,
    ) {
        suspend operator fun invoke(): AppResult<TodayVerification> = repository.getTodayVerification()
    }

class GetMyVerificationsUseCase
    @Inject
    constructor(
        private val repository: VerificationRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<MyVerification>> = repository.getMyVerifications()
    }
