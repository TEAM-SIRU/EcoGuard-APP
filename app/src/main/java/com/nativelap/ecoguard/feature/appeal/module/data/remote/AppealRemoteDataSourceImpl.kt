package com.nativelap.ecoguard.feature.appeal.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.appeal.module.data.dto.AppealDecisionRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.AppealResponseDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.CreateAppealRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.MyAppealResponseDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AppealRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: AppealApiService,
        private val apiExecutor: ApiExecutor,
    ) : AppealRemoteDataSource {
        override suspend fun createAppealWithPhotos(
            verificationId: Long,
            content: String,
            photos: List<File>,
        ): NetworkResult<Unit> {
            val photoParts =
                photos.map { photo ->
                    MultipartBody.Part.createFormData(
                        "photos",
                        photo.name,
                        photo.asRequestBody("application/octet-stream".toMediaTypeOrNull()),
                    )
                }
            return apiExecutor.executeNoContent {
                apiService.createAppealWithPhotos(
                    verificationId = verificationId,
                    content = content,
                    photos = photoParts,
                )
            }
        }

        override suspend fun createAppeal(
            verificationId: Long,
            request: CreateAppealRequestDto,
        ): NetworkResult<Unit> =
            apiExecutor.executeNoContent {
                apiService.createAppeal(verificationId, request)
            }

        override suspend fun getAppeals(status: String?): NetworkResult<List<AppealResponseDto>> =
            apiExecutor.execute { apiService.getAppeals(status) }

        override suspend fun getMyAppeals(): NetworkResult<List<MyAppealResponseDto>> =
            apiExecutor.execute { apiService.getMyAppeals() }

        override suspend fun decideAppeal(
            appealId: Long,
            request: AppealDecisionRequestDto,
        ): NetworkResult<Unit> =
            apiExecutor.executeNoContent {
                apiService.decideAppeal(appealId, request)
            }
    }
