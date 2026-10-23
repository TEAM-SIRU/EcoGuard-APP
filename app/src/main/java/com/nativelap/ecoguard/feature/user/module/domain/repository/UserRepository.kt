package com.nativelap.ecoguard.feature.user.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile

interface UserRepository {
    suspend fun getMyProfile(): AppResult<UserProfile>

    suspend fun withdraw(): AppResult<Unit>

    suspend fun logout(accessToken: String): AppResult<Unit>
}
