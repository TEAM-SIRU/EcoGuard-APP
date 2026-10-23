package com.nativelap.ecoguard.core.network.auth

interface SessionTokenAccess : AccessTokenProvider {
    fun getRefreshToken(): String?

    fun updateTokens(
        expectedRefreshToken: String,
        accessToken: String,
        refreshToken: String,
    ): Boolean

    fun expireSession(expectedRefreshToken: String? = null): Boolean
}
