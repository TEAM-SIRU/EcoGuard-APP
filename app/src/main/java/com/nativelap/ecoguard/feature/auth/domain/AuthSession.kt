package com.nativelap.ecoguard.feature.auth.domain

data class AuthUser(
    val userId: Long?,
    val name: String?,
    val role: String?,
    val studentNumber: String?,
    val grade: Int?,
    val classNo: Int?,
)

data class AuthSession(
    val sessionId: String,
    val accessToken: String,
    val refreshToken: String,
    val user: AuthUser?,
)
