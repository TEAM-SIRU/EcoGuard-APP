package com.nativelap.ecoguard.feature.appeal.module.data.remote

import com.nativelap.ecoguard.feature.appeal.module.data.dto.CreateAppealRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.MyAppealResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AppealApiService {
    @Multipart
    @POST("api/v1/verifications/{verificationId}/appeals")
    suspend fun createAppealWithPhotos(
        @Path("verificationId") verificationId: Long,
        @Part("content") content: okhttp3.RequestBody,
        @Part photos: List<MultipartBody.Part>,
    ): Response<Unit>

    @POST("api/v1/verifications/{verificationId}/appeals")
    suspend fun createAppeal(
        @Path("verificationId") verificationId: Long,
        @Body request: CreateAppealRequestDto,
    ): Response<Unit>

    @GET("api/v1/appeals/me")
    suspend fun getMyAppeals(): Response<List<MyAppealResponseDto>>
}
