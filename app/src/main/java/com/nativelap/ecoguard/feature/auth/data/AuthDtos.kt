package com.nativelap.ecoguard.feature.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizeRequestDto(
    @SerialName("handoffChallenge") val handoffChallenge: String,
    @SerialName("challengeMethod") val challengeMethod: String,
)

@Serializable
data class AuthorizeResponseDto(
    @SerialName("authorizationUrl") val authorizationUrl: String,
)

@Serializable
data class ExchangeRequestDto(
    @SerialName("handoffCode") val handoffCode: String,
    @SerialName("handoffVerifier") val handoffVerifier: String,
)

@Serializable
data class AuthSessionResponseDto(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("user") val user: AuthUserDto? = null,
)

@Serializable
data class AuthUserDto(
    @SerialName("userId") val userId: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("role") val role: String? = null,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("grade") val grade: Int? = null,
    @SerialName("classNo") val classNo: Int? = null,
)
