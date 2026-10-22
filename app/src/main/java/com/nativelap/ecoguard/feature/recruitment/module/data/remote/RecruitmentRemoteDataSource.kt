package com.nativelap.ecoguard.feature.recruitment.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicantResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicationStatusResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CreateRecruitmentRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CreateRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CurrentRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.RecruitmentSummaryResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.UpdateRecruitmentRequestDto
import javax.inject.Inject

interface RecruitmentRemoteDataSource {
    suspend fun getRecruitments(): NetworkResult<List<RecruitmentSummaryResponseDto>>

    suspend fun createRecruitment(request: CreateRecruitmentRequestDto): NetworkResult<CreateRecruitmentResponseDto>

    suspend fun getApplicants(recruitmentId: Long): NetworkResult<List<ApplicantResponseDto>>

    suspend fun apply(
        recruitmentId: Long,
        request: ApplyRequestDto,
    ): NetworkResult<ApplyResponseDto>

    suspend fun deleteRecruitment(recruitmentId: Long): NetworkResult<Unit>

    suspend fun updateRecruitment(
        recruitmentId: Long,
        request: UpdateRecruitmentRequestDto,
    ): NetworkResult<Unit>

    suspend fun getCurrentRecruitment(): NetworkResult<CurrentRecruitmentResponseDto>

    suspend fun getMyApplicationStatus(): NetworkResult<ApplicationStatusResponseDto>
}

class RecruitmentRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: RecruitmentApiService,
        private val apiExecutor: ApiExecutor,
    ) : RecruitmentRemoteDataSource {
        override suspend fun getRecruitments(): NetworkResult<List<RecruitmentSummaryResponseDto>> =
            apiExecutor.execute(apiService::getRecruitments)

        override suspend fun createRecruitment(
            request: CreateRecruitmentRequestDto,
        ): NetworkResult<CreateRecruitmentResponseDto> = apiExecutor.execute { apiService.createRecruitment(request) }

        override suspend fun getApplicants(recruitmentId: Long): NetworkResult<List<ApplicantResponseDto>> =
            apiExecutor.execute { apiService.getApplicants(recruitmentId) }

        override suspend fun apply(
            recruitmentId: Long,
            request: ApplyRequestDto,
        ): NetworkResult<ApplyResponseDto> = apiExecutor.execute { apiService.apply(recruitmentId, request) }

        override suspend fun deleteRecruitment(recruitmentId: Long): NetworkResult<Unit> =
            apiExecutor.executeNoContent { apiService.deleteRecruitment(recruitmentId) }

        override suspend fun updateRecruitment(
            recruitmentId: Long,
            request: UpdateRecruitmentRequestDto,
        ): NetworkResult<Unit> = apiExecutor.executeNoContent { apiService.updateRecruitment(recruitmentId, request) }

        override suspend fun getCurrentRecruitment(): NetworkResult<CurrentRecruitmentResponseDto> =
            apiExecutor.execute(apiService::getCurrentRecruitment)

        override suspend fun getMyApplicationStatus(): NetworkResult<ApplicationStatusResponseDto> =
            apiExecutor.execute(apiService::getMyApplicationStatus)
    }
