package com.nativelap.ecoguard.feature.user.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.user.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.user.module.data.remote.UserRemoteDataSource
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile
import com.nativelap.ecoguard.feature.user.module.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: UserRemoteDataSource,
    ) : UserRepository {
        override suspend fun getMyProfile(): AppResult<UserProfile> =
            when (val result = remoteDataSource.getMyProfile()) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun withdraw(): AppResult<Unit> =
            when (val result = remoteDataSource.withdraw()) {
                is NetworkResult.Success -> AppResult.Success(Unit)
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun logout(accessToken: String): AppResult<Unit> =
            when (val result = remoteDataSource.logout(accessToken)) {
                is NetworkResult.Success -> AppResult.Success(Unit)
                is NetworkResult.Failure -> result.toAppResult()
            }
    }
