package com.nativelap.ecoguard.feature.login.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val isLoggingIn: Boolean = false,
    val hasLoginFailed: Boolean = false,
)
