package com.nativelap.ecoguard.core.network

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult

fun NetworkResult.Failure.toAppResult(): AppResult.Failure =
    AppResult.Failure(
        error =
            when (val networkError = error) {
                is NetworkError.Http -> AppError.Http(networkError.statusCode)
                NetworkError.Connectivity -> AppError.Connectivity
                NetworkError.InvalidResponse -> AppError.InvalidResponse
                NetworkError.Unknown -> AppError.Unknown
            },
    )
