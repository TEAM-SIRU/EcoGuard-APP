package com.nativelap.ecoguard.feature.user.module.domain.model

enum class UserRole {
    STUDENT,
    TEACHER,
    UNKNOWN,
}

data class UserProfile(
    val userId: Long?,
    val name: String?,
    val role: UserRole,
    val studentNumber: String?,
    val grade: Int?,
    val classNo: Int?,
)
