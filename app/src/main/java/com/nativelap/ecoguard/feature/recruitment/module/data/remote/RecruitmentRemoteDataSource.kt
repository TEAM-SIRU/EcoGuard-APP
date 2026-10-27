package com.nativelap.ecoguard.feature.recruitment.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicationStatusResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CurrentRecruitmentResponseDto
import javax.inject.Inject

interface RecruitmentRemoteDataSource {
    suspend fun apply(
        recruitmentId: Long,
        request: ApplyRequestDto,
    ): NetworkResult<ApplyResponseDto>

    suspend fun getCurrentRecruitment(): NetworkResult<CurrentRecruitmentResponseDto>

    suspend fun getMyApplicationStatus(): NetworkResult<ApplicationStatusResponseDto>
}

class RecruitmentRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: RecruitmentApiService,
        private val apiExecutor: ApiExecutor,
    ) : RecruitmentRemoteDataSource {
        override suspend fun apply(
            recruitmentId: Long,
            request: ApplyRequestDto,
        ): NetworkResult<ApplyResponseDto> = apiExecutor.execute { apiService.apply(recruitmentId, request) }

        override suspend fun getCurrentRecruitment(): NetworkResult<CurrentRecruitmentResponseDto> =
            apiExecutor.execute(apiService::getCurrentRecruitment)

        override suspend fun getMyApplicationStatus(): NetworkResult<ApplicationStatusResponseDto> =
            apiExecutor.execute(apiService::getMyApplicationStatus)
    }
