package com.nativelap.ecoguard.feature.user.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.user.module.data.dto.MyProfileResponseDto
import javax.inject.Inject

class UserRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: UserApiService,
        private val apiExecutor: ApiExecutor,
    ) : UserRemoteDataSource {
        override suspend fun getMyProfile(): NetworkResult<MyProfileResponseDto> =
            apiExecutor.execute { apiService.getMyProfile() }

        override suspend fun withdraw(): NetworkResult<Unit> = apiExecutor.executeNoContent { apiService.withdraw() }

        override suspend fun logout(accessToken: String): NetworkResult<Unit> =
            apiExecutor.executeNoContent { apiService.logout("Bearer $accessToken") }
    }
