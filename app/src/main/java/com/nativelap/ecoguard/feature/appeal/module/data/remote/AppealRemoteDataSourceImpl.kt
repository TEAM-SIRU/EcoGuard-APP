package com.nativelap.ecoguard.feature.appeal.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.appeal.module.data.dto.CreateAppealRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.MyAppealResponseDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
                    val mediaType =
                        when (photo.extension.lowercase()) {
                            "jpg", "jpeg" -> "image/jpeg"
                            "png" -> "image/png"
                            "webp" -> "image/webp"
                            else -> "application/octet-stream"
                        }
                    MultipartBody.Part.createFormData(
                        "photos",
                        photo.name,
                        photo.asRequestBody(mediaType.toMediaTypeOrNull()),
                    )
                }
            return apiExecutor.executeNoContent {
                apiService.createAppealWithPhotos(
                    verificationId = verificationId,
                    content = content.toRequestBody("text/plain".toMediaTypeOrNull()),
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

        override suspend fun getMyAppeals(): NetworkResult<List<MyAppealResponseDto>> =
            apiExecutor.execute { apiService.getMyAppeals() }
    }
