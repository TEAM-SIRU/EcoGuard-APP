package com.nativelap.ecoguard.feature.recruitment.module.data.remote

import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicationStatusResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CurrentRecruitmentResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecruitmentApiService {
    @POST("api/v1/recruitments/{recruitmentId}/applications")
    suspend fun apply(
        @Path("recruitmentId") recruitmentId: Long,
        @Body request: ApplyRequestDto,
    ): Response<ApplyResponseDto>

    @GET("api/v1/recruitments/current")
    suspend fun getCurrentRecruitment(): Response<CurrentRecruitmentResponseDto>

    @GET("api/v1/applications/me")
    suspend fun getMyApplicationStatus(): Response<ApplicationStatusResponseDto>
}
