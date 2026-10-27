package com.nativelap.ecoguard.feature.auth.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.auth.domain.CancelAuthUseCase
import com.nativelap.ecoguard.feature.auth.domain.ClearSessionAfterWithdrawalUseCase
import com.nativelap.ecoguard.feature.auth.domain.CompleteAuthUseCase
import com.nativelap.ecoguard.feature.auth.domain.ObserveSessionUseCase
import com.nativelap.ecoguard.feature.auth.domain.SignOutUseCase
import com.nativelap.ecoguard.feature.auth.domain.StartAuthUseCase
import com.nativelap.ecoguard.feature.auth.domain.VerifyAuthAttemptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val observeSessionUseCase: ObserveSessionUseCase,
        private val startAuthUseCase: StartAuthUseCase,
        private val completeAuthUseCase: CompleteAuthUseCase,
        private val verifyAuthAttemptUseCase: VerifyAuthAttemptUseCase,
        private val cancelAuthUseCase: CancelAuthUseCase,
        private val signOutUseCase: SignOutUseCase,
        private val clearSessionAfterWithdrawal: ClearSessionAfterWithdrawalUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow(AuthUiState())
        val uiState = mutableUiState.asStateFlow()
        private var awaitingExternalAuth = false

        init {
            viewModelScope.launch {
                observeSessionUseCase().collect { session ->
                    mutableUiState.update { state -> state.copy(sessionId = session?.sessionId) }
                }
            }
        }

        fun startLogin() {
            if (mutableUiState.value.isLoggingIn) return
            val previousState = mutableUiState.value
            val retryLogoutBeforeLogin = previousState.hasSessionClearFailed || previousState.hasRetryableLogoutPending
            if (retryLogoutBeforeLogin) {
                mutableUiState.update { state -> state.copy(isLoggingIn = true) }
            }
            viewModelScope.launch {
                if (retryLogoutBeforeLogin) {
                    val retryResult = signOutUseCase()
                    if (!retryResult.localSessionCleared) {
                        mutableUiState.update { state ->
                            state.copy(
                                isLoggingIn = false,
                                hasSessionClearFailed = true,
                            )
                        }
                        return@launch
                    }
                    mutableUiState.update { state ->
                        state.copy(
                            hasServerLogoutFailed = retryResult.serverLogoutFailed,
                            hasRetryableLogoutPending = retryResult.hasRetryableLogoutPending,
                        )
                    }
                }
                mutableUiState.update { state ->
                    state.copy(
                        isLoggingIn = true,
                        hasLoginFailed = false,
                        hasSessionClearFailed = false,
                        hasServerLogoutFailed = state.hasServerLogoutFailed && state.hasRetryableLogoutPending,
                    )
                }
                when (val result = startAuthUseCase()) {
                    is NetworkResult.Success -> {
                        mutableUiState.update { state ->
                            state.copy(authorizationUrl = result.value)
                        }
                    }

                    is NetworkResult.Failure -> {
                        showLoginFailure()
                    }
                }
            }
        }

        fun onAuthorizationUrlHandled(opened: Boolean) {
            mutableUiState.update { state -> state.copy(authorizationUrl = null) }
            if (opened) {
                awaitingExternalAuth = true
            } else {
                cancelAuthUseCase()
                showLoginFailure()
            }
        }

        fun onExternalAuthReturned() {
            if (!awaitingExternalAuth) return
            awaitingExternalAuth = false
            if (mutableUiState.value.sessionId == null) {
                showLoginFailure()
            }
        }

        fun handleHandoffLink(uri: Uri): Boolean {
            if (!uri.isAllowedHandoffLink()) return false
            awaitingExternalAuth = false
            val handoffCode = uri.getQueryParameter(HANDOFF_CODE_PARAMETER)
            val challenge = uri.getQueryParameter(HANDOFF_CHALLENGE_PARAMETER)
            val error = uri.getQueryParameter(ERROR_PARAMETER)
            mutableUiState.update { state -> state.copy(isLoggingIn = true, hasLoginFailed = false) }
            viewModelScope.launch {
                if (challenge.isNullOrBlank() || !verifyAuthAttemptUseCase(challenge)) {
                    showLoginFailure()
                } else if (!error.isNullOrBlank() || handoffCode.isNullOrBlank()) {
                    showLoginFailure()
                } else {
                    when (completeAuthUseCase(handoffCode, challenge)) {
                        is NetworkResult.Success -> mutableUiState.update { state -> state.copy(isLoggingIn = false) }
                        is NetworkResult.Failure -> showLoginFailure()
                    }
                }
            }
            return true
        }

        fun signOut() {
            if (mutableUiState.value.isLoggingIn) return
            mutableUiState.update { state -> state.copy(isLoggingIn = true) }
            viewModelScope.launch {
                val result = signOutUseCase()
                mutableUiState.update { state ->
                    state.copy(
                        isLoggingIn = false,
                        hasSessionClearFailed = !result.localSessionCleared,
                        hasServerLogoutFailed = result.serverLogoutFailed,
                        hasRetryableLogoutPending = result.hasRetryableLogoutPending,
                    )
                }
            }
        }

        fun onWithdrawalSucceeded() {
            val cleared = clearSessionAfterWithdrawal()
            mutableUiState.update { state ->
                state.copy(
                    isLoggingIn = false,
                    hasSessionClearFailed = !cleared,
                    hasLoginFailed = false,
                )
            }
        }

        private fun showLoginFailure() {
            mutableUiState.update { state ->
                state.copy(isLoggingIn = false, hasLoginFailed = true, authorizationUrl = null)
            }
        }

        private fun Uri.isAllowedHandoffLink(): Boolean =
            scheme == HTTPS_SCHEME &&
                host == APP_LINK_HOST &&
                (port == -1 || port == HTTPS_PORT) &&
                path == APP_LINK_PATH &&
                userInfo == null &&
                fragment == null &&
                queryParameterNames.all { name -> name in ALLOWED_CALLBACK_PARAMETERS } &&
                getQueryParameters(HANDOFF_CODE_PARAMETER).size <= 1 &&
                getQueryParameters(HANDOFF_CHALLENGE_PARAMETER).size <= 1 &&
                getQueryParameters(ERROR_PARAMETER).size <= 1

        private companion object {
            const val HTTPS_SCHEME = "https"
            const val HTTPS_PORT = 443
            const val APP_LINK_HOST = "ecoguard.https.gsmsv.site"
            const val APP_LINK_PATH = "/auth/mobile-callback"
            const val HANDOFF_CODE_PARAMETER = "handoff_code"
            const val HANDOFF_CHALLENGE_PARAMETER = "handoff_challenge"
            const val ERROR_PARAMETER = "error"
            val ALLOWED_CALLBACK_PARAMETERS =
                setOf(HANDOFF_CODE_PARAMETER, HANDOFF_CHALLENGE_PARAMETER, ERROR_PARAMETER)
        }
    }
