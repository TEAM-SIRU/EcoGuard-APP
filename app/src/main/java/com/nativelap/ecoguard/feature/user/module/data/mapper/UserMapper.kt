package com.nativelap.ecoguard.feature.user.module.data.mapper

import com.nativelap.ecoguard.feature.user.module.data.dto.MyProfileResponseDto
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile
import com.nativelap.ecoguard.feature.user.module.domain.model.UserRole

fun MyProfileResponseDto.toDomain(): UserProfile =
    UserProfile(
        userId = userId,
        name = name,
        role = role.toUserRole(),
        studentNumber = studentNumber,
        grade = grade,
        classNo = classNo,
    )

private fun String?.toUserRole(): UserRole =
    when (this) {
        "STUDENT" -> UserRole.STUDENT
        "TEACHER" -> UserRole.TEACHER
        else -> UserRole.UNKNOWN
    }
