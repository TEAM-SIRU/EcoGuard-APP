package com.nativelap.ecoguard.feature.auth.data

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRemoteDataSource {
    suspend fun authorize(challenge: String): NetworkResult<AuthorizeResponseDto>

    suspend fun exchange(
        code: String,
        verifier: String,
    ): NetworkResult<AuthSessionResponseDto>
}

@Singleton
class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val api: AuthApiService,
        private val executor: ApiExecutor,
    ) : AuthRemoteDataSource {
        override suspend fun authorize(challenge: String): NetworkResult<AuthorizeResponseDto> =
            executor.execute { api.authorize(AuthorizeRequestDto(challenge, "S256")) }

        override suspend fun exchange(
            code: String,
            verifier: String,
        ): NetworkResult<AuthSessionResponseDto> = executor.execute { api.exchange(ExchangeRequestDto(code, verifier)) }
    }
