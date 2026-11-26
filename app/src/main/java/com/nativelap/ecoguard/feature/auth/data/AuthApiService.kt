package com.nativelap.ecoguard.feature.auth.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/v1/auth/authorize")
    suspend fun authorize(
        @Body request: AuthorizeRequestDto,
    ): Response<AuthorizeResponseDto>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto,
    ): Response<AuthSessionResponseDto>
}
