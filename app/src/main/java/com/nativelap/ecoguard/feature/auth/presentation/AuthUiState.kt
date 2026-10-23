package com.nativelap.ecoguard.feature.auth.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class AuthUiState(
    val sessionId: String? = null,
    val isLoggingIn: Boolean = false,
    val hasLoginFailed: Boolean = false,
    val hasSessionClearFailed: Boolean = false,
    val hasServerLogoutFailed: Boolean = false,
    val hasRetryableLogoutPending: Boolean = false,
    val authorizationUrl: String? = null,
)
