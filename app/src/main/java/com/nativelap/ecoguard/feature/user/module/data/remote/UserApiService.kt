package com.nativelap.ecoguard.feature.user.module.data.remote

import com.nativelap.ecoguard.feature.user.module.data.dto.MyProfileResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {
    @GET("api/v1/users/me")
    suspend fun getMyProfile(): Response<MyProfileResponseDto>

    @DELETE("api/v1/users/me")
    suspend fun withdraw(): Response<Unit>

    @POST("api/v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") authorization: String,
    ): Response<Unit>
}
