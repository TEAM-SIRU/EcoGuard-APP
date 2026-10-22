package com.nativelap.ecoguard.core.domain.result

sealed interface AppResult<out T> {
    data class Success<T>(
        val value: T,
    ) : AppResult<T>

    data class Failure(
        val error: AppError,
    ) : AppResult<Nothing>
}

sealed interface AppError {
    data class Http(
        val statusCode: Int,
    ) : AppError

    data object Connectivity : AppError

    data object InvalidResponse : AppError

    data object Unknown : AppError
}
