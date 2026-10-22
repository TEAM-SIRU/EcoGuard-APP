package com.nativelap.ecoguard.feature.verification.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewDecisionRequestDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewItemResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.MyVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ReviewResultResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.SubmitVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.TodayVerificationResponseDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class VerificationRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: VerificationApiService,
        private val apiExecutor: ApiExecutor,
    ) : VerificationRemoteDataSource {
        override suspend fun submitVerification(
            photo: File,
            areaId: Long?,
            idempotencyKey: String?,
            submitStartedAt: String?,
        ): NetworkResult<SubmitVerificationResponseDto> {
            val mediaType =
                when (photo.extension.lowercase()) {
                    "jpg", "jpeg" -> "image/jpeg"
                    "png" -> "image/png"
                    else -> "application/octet-stream"
                }.toMediaType()
            val photoPart =
                MultipartBody.Part.createFormData(
                    name = PHOTO_PART_NAME,
                    filename = photo.name,
                    body = photo.asRequestBody(mediaType),
                )
            return apiExecutor.execute {
                apiService.submitVerification(
                    areaId = areaId,
                    idempotencyKey = idempotencyKey,
                    submitStartedAt = submitStartedAt,
                    photo = photoPart,
                )
            }
        }

        override suspend fun getManualReviewQueue(): NetworkResult<List<ManualReviewItemResponseDto>> =
            apiExecutor.execute { apiService.getManualReviewQueue() }

        override suspend fun getReviewResult(verificationId: Long): NetworkResult<ReviewResultResponseDto> =
            apiExecutor.execute { apiService.getReviewResult(verificationId) }

        override suspend fun decideReview(
            verificationId: Long,
            request: ManualReviewDecisionRequestDto,
        ): NetworkResult<Unit> = apiExecutor.executeNoContent { apiService.decideReview(verificationId, request) }

        override suspend fun getTodayVerification(): NetworkResult<TodayVerificationResponseDto> =
            apiExecutor.execute { apiService.getTodayVerification() }

        override suspend fun getMyVerifications(): NetworkResult<List<MyVerificationResponseDto>> =
            apiExecutor.execute { apiService.getMyVerifications() }

        private companion object {
            const val PHOTO_PART_NAME = "photo"
        }
    }
