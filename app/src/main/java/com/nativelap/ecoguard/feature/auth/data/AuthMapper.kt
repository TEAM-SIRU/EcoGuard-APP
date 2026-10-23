package com.nativelap.ecoguard.feature.auth.data

import com.nativelap.ecoguard.feature.auth.domain.AuthSession
import com.nativelap.ecoguard.feature.auth.domain.AuthUser

fun AuthSessionResponseDto.toDomain(sessionId: String): AuthSession =
    AuthSession(
        sessionId = sessionId,
        accessToken = accessToken,
        refreshToken = refreshToken,
        user =
            user?.let { dto ->
                AuthUser(
                    userId = dto.userId,
                    name = dto.name,
                    role = dto.role,
                    studentNumber = dto.studentNumber,
                    grade = dto.grade,
                    classNo = dto.classNo,
                )
            },
    )
