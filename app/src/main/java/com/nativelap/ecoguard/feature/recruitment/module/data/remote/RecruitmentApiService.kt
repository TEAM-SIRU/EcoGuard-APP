package com.nativelap.ecoguard.feature.recruitment.module.data.remote

import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicantResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicationStatusResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CreateRecruitmentRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CreateRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CurrentRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.RecruitmentSummaryResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.UpdateRecruitmentRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RecruitmentApiService {
    @GET("api/v1/recruitments")
    suspend fun getRecruitments(): Response<List<RecruitmentSummaryResponseDto>>

    @POST("api/v1/recruitments")
    suspend fun createRecruitment(
        @Body request: CreateRecruitmentRequestDto,
    ): Response<CreateRecruitmentResponseDto>

    @GET("api/v1/recruitments/{recruitmentId}/applications")
    suspend fun getApplicants(
        @Path("recruitmentId") recruitmentId: Long,
    ): Response<List<ApplicantResponseDto>>

    @POST("api/v1/recruitments/{recruitmentId}/applications")
    suspend fun apply(
        @Path("recruitmentId") recruitmentId: Long,
        @Body request: ApplyRequestDto,
    ): Response<ApplyResponseDto>

    @DELETE("api/v1/recruitments/{recruitmentId}")
    suspend fun deleteRecruitment(
        @Path("recruitmentId") recruitmentId: Long,
    ): Response<Unit>

    @PATCH("api/v1/recruitments/{recruitmentId}")
    suspend fun updateRecruitment(
        @Path("recruitmentId") recruitmentId: Long,
        @Body request: UpdateRecruitmentRequestDto,
    ): Response<Unit>

    @GET("api/v1/recruitments/current")
    suspend fun getCurrentRecruitment(): Response<CurrentRecruitmentResponseDto>

    @GET("api/v1/applications/me")
    suspend fun getMyApplicationStatus(): Response<ApplicationStatusResponseDto>
}
