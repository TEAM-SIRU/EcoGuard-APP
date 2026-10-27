package com.nativelap.ecoguard.feature.appeal.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.appeal.module.data.dto.CreateAppealRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.appeal.module.data.remote.AppealRemoteDataSource
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import java.io.File
import javax.inject.Inject

class AppealRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: AppealRemoteDataSource,
    ) : AppealRepository {
        override suspend fun createAppealWithPhotos(
            verificationId: Long,
            content: String,
            photos: List<File>,
        ): AppResult<Unit> =
            remoteDataSource
                .createAppealWithPhotos(
                    verificationId = verificationId,
                    content = content,
                    photos = photos,
                ).toAppResultOrSuccess()

        override suspend fun createAppeal(
            verificationId: Long,
            content: String,
        ): AppResult<Unit> =
            remoteDataSource
                .createAppeal(
                    verificationId = verificationId,
                    request = CreateAppealRequestDto(content),
                ).toAppResultOrSuccess()

        override suspend fun getMyAppeals(): AppResult<List<MyAppeal>> =
            when (val result = remoteDataSource.getMyAppeals()) {
                is NetworkResult.Success -> AppResult.Success(result.value.map { it.toDomain() })
                is NetworkResult.Failure -> result.toAppResult()
            }

        private fun NetworkResult<Unit>.toAppResultOrSuccess(): AppResult<Unit> =
            when (this) {
                is NetworkResult.Success -> AppResult.Success(value)
                is NetworkResult.Failure -> toAppResult()
            }
    }
