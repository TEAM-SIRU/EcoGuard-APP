package com.nativelap.ecoguard.feature.auth.domain

import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val session: StateFlow<AuthSession?>

    fun accessToken(): String?

    fun clearLocalSession(): Boolean

    fun pendingLogoutAccessTokens(): List<String>

    fun rememberPendingLogout(accessToken: String)

    fun clearPendingLogout(accessToken: String)
}
