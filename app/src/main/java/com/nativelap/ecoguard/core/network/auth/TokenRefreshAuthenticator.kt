package com.nativelap.ecoguard.core.network.auth

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

class TokenRefreshAuthenticator
    @Inject
    constructor(
        private val sessionTokenAccess: SessionTokenAccess,
        private val tokenRefreshApi: TokenRefreshApi,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (response.responseCount() >= MAX_AUTH_ATTEMPTS) return null

            val rejectedAccessToken = response.request.header(AUTHORIZATION_HEADER)?.removePrefix(BEARER_PREFIX)
            val latestAccessToken = sessionTokenAccess.getAccessToken()
            if (latestAccessToken != null && latestAccessToken != rejectedAccessToken) {
                return response.request.withAccessToken(latestAccessToken)
            }

            synchronized(this) {
                val currentAccessToken = sessionTokenAccess.getAccessToken()
                if (currentAccessToken != null && currentAccessToken != rejectedAccessToken) {
                    return response.request.withAccessToken(currentAccessToken)
                }

                val refreshToken = sessionTokenAccess.getRefreshToken() ?: return null
                val refreshedTokens = refreshTokens(refreshToken)
                if (refreshedTokens == null) {
                    sessionTokenAccess.expireSession(refreshToken)
                    return null
                }

                if (!sessionTokenAccess.updateTokens(
                        expectedRefreshToken = refreshToken,
                        accessToken = refreshedTokens.accessToken,
                        refreshToken = refreshedTokens.refreshToken,
                    )
                ) {
                    return sessionTokenAccess.getAccessToken()?.let { token -> response.request.withAccessToken(token) }
                }

                return response.request.withAccessToken(refreshedTokens.accessToken)
            }
        }

        private fun refreshTokens(refreshToken: String): RefreshedTokens? =
            try {
                val response = tokenRefreshApi.refresh(TokenRefreshRequestDto(refreshToken))
                if (!response.isSuccessful) {
                    response.raw().close()
                    null
                } else {
                    val responseBody = response.body()
                    response.raw().close()
                    val accessToken = responseBody?.accessToken?.takeIf(String::isNotBlank) ?: return null
                    val updatedRefreshToken = responseBody.refreshToken?.takeIf(String::isNotBlank) ?: return null
                    RefreshedTokens(accessToken, updatedRefreshToken)
                }
            } catch (exception: IOException) {
                null
            } catch (exception: Exception) {
                null
            }

        private fun Request.withAccessToken(accessToken: String): Request =
            newBuilder()
                .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$accessToken")
                .build()

        private fun Response.responseCount(): Int {
            var count = 1
            var previousResponse = priorResponse
            while (previousResponse != null) {
                count += 1
                previousResponse = previousResponse.priorResponse
            }
            return count
        }

        private data class RefreshedTokens(
            val accessToken: String,
            val refreshToken: String,
        )

        private companion object {
            const val AUTHORIZATION_HEADER = "Authorization"
            const val BEARER_PREFIX = "Bearer "
            const val MAX_AUTH_ATTEMPTS = 2
        }
    }
