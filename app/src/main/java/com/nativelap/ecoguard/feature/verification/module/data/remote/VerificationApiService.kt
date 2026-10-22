package com.nativelap.ecoguard.feature.verification.module.data.remote

import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewDecisionRequestDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewItemResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.MyVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ReviewResultResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.SubmitVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.TodayVerificationResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface VerificationApiService {
    @GET("api/v1/verifications")
    suspend fun getManualReviewQueue(): Response<List<ManualReviewItemResponseDto>>

    @Multipart
    @POST("api/v1/verifications")
    suspend fun submitVerification(
        @Query("areaId") areaId: Long?,
        @Header("Idempotency-Key") idempotencyKey: String?,
        @Header("X-Submit-Started-At") submitStartedAt: String?,
        @Part photo: MultipartBody.Part,
    ): Response<SubmitVerificationResponseDto>

    @GET("api/v1/verifications/{verificationId}/review")
    suspend fun getReviewResult(
        @Path("verificationId") verificationId: Long,
    ): Response<ReviewResultResponseDto>

    @PATCH("api/v1/verifications/{verificationId}/review")
    suspend fun decideReview(
        @Path("verificationId") verificationId: Long,
        @Body request: ManualReviewDecisionRequestDto,
    ): Response<Unit>

    @GET("api/v1/verifications/today")
    suspend fun getTodayVerification(): Response<TodayVerificationResponseDto>

    @GET("api/v1/verifications/me")
    suspend fun getMyVerifications(): Response<List<MyVerificationResponseDto>>
}
