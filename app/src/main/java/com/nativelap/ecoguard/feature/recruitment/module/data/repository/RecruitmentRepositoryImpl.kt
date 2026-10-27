package com.nativelap.ecoguard.feature.recruitment.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.recruitment.module.data.remote.RecruitmentRemoteDataSource
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import javax.inject.Inject

class RecruitmentRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: RecruitmentRemoteDataSource,
    ) : RecruitmentRepository {
        override suspend fun apply(command: ApplyCommand): AppResult<ApplyResult> =
            remoteDataSource
                .apply(
                    recruitmentId = command.recruitmentId,
                    request = ApplyRequestDto(motivation = command.motivation),
                ).map { it.toDomain() }

        override suspend fun getCurrentRecruitment(): AppResult<CurrentRecruitment> =
            remoteDataSource.getCurrentRecruitment().map { it.toDomain() }

        override suspend fun getMyApplicationStatus(): AppResult<MyApplicationStatus> =
            remoteDataSource.getMyApplicationStatus().map { it.toDomain() }

        private inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): AppResult<R> =
            when (this) {
                is NetworkResult.Success -> AppResult.Success(transform(value))
                is NetworkResult.Failure -> toAppResult()
            }
    }
