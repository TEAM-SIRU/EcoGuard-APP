package com.nativelap.ecoguard.feature.verification.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewDecisionRequestDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewItemResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.MyVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ReviewResultResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.SubmitVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.TodayVerificationResponseDto
import java.io.File

interface VerificationRemoteDataSource {
    suspend fun submitVerification(
        photo: File,
        areaId: Long?,
        idempotencyKey: String?,
        submitStartedAt: String?,
    ): NetworkResult<SubmitVerificationResponseDto>

    suspend fun getManualReviewQueue(): NetworkResult<List<ManualReviewItemResponseDto>>

    suspend fun getReviewResult(verificationId: Long): NetworkResult<ReviewResultResponseDto>

    suspend fun decideReview(
        verificationId: Long,
        request: ManualReviewDecisionRequestDto,
    ): NetworkResult<Unit>

    suspend fun getTodayVerification(): NetworkResult<TodayVerificationResponseDto>

    suspend fun getMyVerifications(): NetworkResult<List<MyVerificationResponseDto>>
}
