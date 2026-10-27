package com.nativelap.ecoguard.feature.appeal.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.appeal.module.data.dto.CreateAppealRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.MyAppealResponseDto
import java.io.File

interface AppealRemoteDataSource {
    suspend fun createAppealWithPhotos(
        verificationId: Long,
        content: String,
        photos: List<File>,
    ): NetworkResult<Unit>

    suspend fun createAppeal(
        verificationId: Long,
        request: CreateAppealRequestDto,
    ): NetworkResult<Unit>

    suspend fun getMyAppeals(): NetworkResult<List<MyAppealResponseDto>>
}
