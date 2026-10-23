package com.nativelap.ecoguard.feature.auth.domain

import com.nativelap.ecoguard.core.network.NetworkResult

interface AuthRepository {
    suspend fun startLogin(): NetworkResult<String>

    suspend fun completeLogin(
        handoffCode: String,
        challenge: String,
    ): NetworkResult<Unit>

    suspend fun matchesPendingAttempt(challenge: String): Boolean

    fun cancelLogin()
}
