package com.nativelap.ecoguard.core.network

sealed interface NetworkResult<out T> {
    data class Success<T>(
        val value: T,
    ) : NetworkResult<T>

    data class Failure(
        val error: NetworkError,
    ) : NetworkResult<Nothing>
}

sealed interface NetworkError {
    data class Http(
        val statusCode: Int,
    ) : NetworkError

    data object Connectivity : NetworkError

    data object InvalidResponse : NetworkError

    data object Unknown : NetworkError
}
