package com.nativelap.ecoguard.feature.recruitment.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CreateRecruitmentRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.UpdateRecruitmentRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.recruitment.module.data.remote.RecruitmentRemoteDataSource
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CreateRecruitmentCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.UpdateRecruitmentCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import javax.inject.Inject

class RecruitmentRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: RecruitmentRemoteDataSource,
    ) : RecruitmentRepository {
        override suspend fun getRecruitments() =
            remoteDataSource.getRecruitments().map { response ->
                response.map { it.toDomain() }
            }

        override suspend fun createRecruitment(command: CreateRecruitmentCommand) =
            remoteDataSource
                .createRecruitment(
                    CreateRecruitmentRequestDto(
                        semester = command.semester,
                        grade = command.grade,
                        classNo = command.classNo,
                        maxCount = command.maxCount,
                        startDate = command.startDate,
                        endDate = command.endDate,
                        activityStartTime = command.activityStartTime,
                        activityEndTime = command.activityEndTime,
                    ),
                ).map { it.toDomain() }

        override suspend fun getApplicants(recruitmentId: Long) =
            remoteDataSource.getApplicants(recruitmentId).map { applicants ->
                applicants.map { it.toDomain() }
            }

        override suspend fun apply(command: ApplyCommand) =
            remoteDataSource
                .apply(
                    recruitmentId = command.recruitmentId,
                    request = ApplyRequestDto(motivation = command.motivation),
                ).map { it.toDomain() }

        override suspend fun deleteRecruitment(recruitmentId: Long) =
            remoteDataSource.deleteRecruitment(recruitmentId).toAppResultUnit()

        override suspend fun updateRecruitment(command: UpdateRecruitmentCommand) =
            remoteDataSource
                .updateRecruitment(
                    recruitmentId = command.recruitmentId,
                    request =
                        UpdateRecruitmentRequestDto(
                            maxCount = command.maxCount,
                            startDate = command.startDate,
                            endDate = command.endDate,
                            activityStartTime = command.activityStartTime,
                            activityEndTime = command.activityEndTime,
                        ),
                ).toAppResultUnit()

        override suspend fun getCurrentRecruitment() = remoteDataSource.getCurrentRecruitment().map { it.toDomain() }

        override suspend fun getMyApplicationStatus() = remoteDataSource.getMyApplicationStatus().map { it.toDomain() }

        private inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): AppResult<R> =
            when (this) {
                is NetworkResult.Success -> AppResult.Success(transform(value))
                is NetworkResult.Failure -> toAppResult()
            }

        private fun NetworkResult<Unit>.toAppResultUnit(): AppResult<Unit> =
            when (this) {
                is NetworkResult.Success -> AppResult.Success(value)
                is NetworkResult.Failure -> toAppResult()
            }
    }
