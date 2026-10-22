package com.nativelap.ecoguard.core.network.auth

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BearerTokenInterceptor
    @Inject
    constructor(
        private val accessTokenProvider: AccessTokenProvider,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val accessToken = accessTokenProvider.getAccessToken()?.takeIf(String::isNotBlank)
            val authenticatedRequest =
                if (accessToken == null) {
                    request
                } else {
                    request
                        .newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .build()
                }

            return chain.proceed(authenticatedRequest)
        }
    }
