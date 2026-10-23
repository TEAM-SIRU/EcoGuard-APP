package com.nativelap.ecoguard.core.network.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshApi {
    @POST("api/v1/auth/refresh")
    fun refresh(
        @Body request: TokenRefreshRequestDto,
    ): Response<TokenRefreshResponseDto>
}
