package com.nativelap.ecoguard.feature.user.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.user.module.data.dto.MyProfileResponseDto

interface UserRemoteDataSource {
    suspend fun getMyProfile(): NetworkResult<MyProfileResponseDto>

    suspend fun withdraw(): NetworkResult<Unit>

    suspend fun logout(): NetworkResult<Unit>
}
