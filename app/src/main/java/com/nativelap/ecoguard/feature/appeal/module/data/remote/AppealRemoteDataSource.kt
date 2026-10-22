package com.nativelap.ecoguard.feature.appeal.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.appeal.module.data.dto.AppealDecisionRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.AppealResponseDto
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

    suspend fun getAppeals(status: String?): NetworkResult<List<AppealResponseDto>>

    suspend fun getMyAppeals(): NetworkResult<List<MyAppealResponseDto>>

    suspend fun decideAppeal(
        appealId: Long,
        request: AppealDecisionRequestDto,
    ): NetworkResult<Unit>
}
