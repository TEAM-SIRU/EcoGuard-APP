package com.nativelap.ecoguard.core.network.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshRequestDto(
    @SerialName("refreshToken") val refreshToken: String,
)

@Serializable
data class TokenRefreshResponseDto(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null,
)
