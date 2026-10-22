package com.nativelap.ecoguard.feature.verification.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.verification.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.verification.module.data.mapper.toRequestDto
import com.nativelap.ecoguard.feature.verification.module.data.remote.VerificationRemoteDataSource
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewDecision
import com.nativelap.ecoguard.feature.verification.module.domain.model.ManualReviewItem
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.ReviewResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.SubmitVerificationResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.TodayVerification
import com.nativelap.ecoguard.feature.verification.module.domain.repository.VerificationRepository
import java.io.File
import javax.inject.Inject

class VerificationRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: VerificationRemoteDataSource,
    ) : VerificationRepository {
        override suspend fun submitVerification(
            photo: File,
            areaId: Long?,
            idempotencyKey: String?,
            submitStartedAt: String?,
        ): AppResult<SubmitVerificationResult> =
            when (
                val result =
                    remoteDataSource.submitVerification(
                        photo = photo,
                        areaId = areaId,
                        idempotencyKey = idempotencyKey,
                        submitStartedAt = submitStartedAt,
                    )
            ) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun getManualReviewQueue(): AppResult<List<ManualReviewItem>> =
            when (val result = remoteDataSource.getManualReviewQueue()) {
                is NetworkResult.Success -> {
                    AppResult.Success(result.value.map { it.toDomain() })
                }

                is NetworkResult.Failure -> {
                    result.toAppResult()
                }
            }

        override suspend fun getReviewResult(verificationId: Long): AppResult<ReviewResult> =
            when (val result = remoteDataSource.getReviewResult(verificationId)) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun decideReview(
            verificationId: Long,
            decision: ManualReviewDecision,
            reason: String?,
        ): AppResult<Unit> =
            when (val result = remoteDataSource.decideReview(verificationId, decision.toRequestDto(reason))) {
                is NetworkResult.Success -> AppResult.Success(Unit)
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun getTodayVerification(): AppResult<TodayVerification> =
            when (val result = remoteDataSource.getTodayVerification()) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun getMyVerifications(): AppResult<List<MyVerification>> =
            when (val result = remoteDataSource.getMyVerifications()) {
                is NetworkResult.Success -> {
                    AppResult.Success(result.value.map { it.toDomain() })
                }

                is NetworkResult.Failure -> {
                    result.toAppResult()
                }
            }
    }
