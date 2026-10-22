package com.nativelap.ecoguard.core.network

import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.CancellationException
import javax.inject.Inject

class ApiExecutor
    @Inject
    constructor() {
        suspend fun <T : Any> execute(request: suspend () -> Response<T>): NetworkResult<T> =
            try {
                val response = request()
                if (!response.isSuccessful) {
                    NetworkResult.Failure(NetworkError.Http(response.code()))
                } else {
                    val body = response.body()
                    if (body == null) {
                        NetworkResult.Failure(NetworkError.InvalidResponse)
                    } else {
                        NetworkResult.Success(body)
                    }
                }
            } catch (exception: Exception) {
                exception.toNetworkFailure()
            }

        suspend fun executeNoContent(request: suspend () -> Response<Unit>): NetworkResult<Unit> =
            try {
                val response = request()
                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Failure(NetworkError.Http(response.code()))
                }
            } catch (exception: Exception) {
                exception.toNetworkFailure()
            }
    }

private fun Exception.toNetworkFailure(): NetworkResult.Failure =
    when (this) {
        is CancellationException -> throw this
        is IOException -> NetworkResult.Failure(NetworkError.Connectivity)
        is SerializationException -> NetworkResult.Failure(NetworkError.InvalidResponse)
        else -> NetworkResult.Failure(NetworkError.Unknown)
    }
