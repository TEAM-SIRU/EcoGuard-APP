package com.nativelap.ecoguard.feature.auth.domain

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

internal fun shouldRetryLogout(error: AppError): Boolean =
    when (error) {
        AppError.Connectivity, AppError.Unknown -> true
        is AppError.Http -> error.statusCode == 408 || error.statusCode == 429 || error.statusCode >= 500
        AppError.InvalidResponse -> false
    }

class StartAuthUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(): NetworkResult<String> = repository.startLogin()
    }

class CompleteAuthUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(
            handoffCode: String,
            challenge: String,
        ): NetworkResult<Unit> = repository.completeLogin(handoffCode, challenge)
    }

class VerifyAuthAttemptUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(challenge: String): Boolean = repository.matchesPendingAttempt(challenge)
    }

class CancelAuthUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        operator fun invoke() = repository.cancelLogin()
    }

class ObserveSessionUseCase
    @Inject
    constructor(
        private val repository: SessionRepository,
    ) {
        operator fun invoke(): StateFlow<AuthSession?> = repository.session
    }

class SignOutUseCase
    @Inject
    constructor(
        private val sessionRepository: SessionRepository,
        private val logoutUseCase: com.nativelap.ecoguard.feature.user.module.domain.usecase.LogoutUseCase,
    ) {
        suspend operator fun invoke(): SignOutResult {
            val tokens =
                (
                    sessionRepository.pendingLogoutAccessTokens() +
                        listOfNotNull(
                            sessionRepository.accessToken(),
                        )
                ).distinct()
            val sessionCleared = sessionRepository.clearLocalSession()
            var serverLogoutFailed = false
            tokens.forEach { token ->
                when (val result = logoutUseCase(token)) {
                    is AppResult.Success -> {
                        sessionRepository.clearPendingLogout(token)
                    }

                    is AppResult.Failure -> {
                        if (shouldRetryLogout(result.error)) {
                            sessionRepository.rememberPendingLogout(token)
                        } else {
                            sessionRepository.clearPendingLogout(token)
                        }
                        serverLogoutFailed = true
                    }
                }
            }
            return SignOutResult(
                localSessionCleared = sessionCleared,
                serverLogoutFailed = serverLogoutFailed,
                hasRetryableLogoutPending = sessionRepository.pendingLogoutAccessTokens().isNotEmpty(),
            )
        }
    }

data class SignOutResult(
    val localSessionCleared: Boolean,
    val serverLogoutFailed: Boolean,
    val hasRetryableLogoutPending: Boolean,
)
