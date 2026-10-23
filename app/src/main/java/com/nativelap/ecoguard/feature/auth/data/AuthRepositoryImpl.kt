package com.nativelap.ecoguard.feature.auth.data

import android.util.Base64
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.auth.domain.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl
    @Inject
    constructor(
        private val remote: AuthRemoteDataSource,
        private val sessionManager: SessionManager,
    ) : AuthRepository {
        private val exchangeMutex = Mutex()

        override suspend fun startLogin(): NetworkResult<String> =
            withContext(Dispatchers.IO) {
                val verifier = randomVerifier()
                val challenge =
                    Base64.encodeToString(
                        MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray(Charsets.US_ASCII)),
                        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING,
                    )
                if (!sessionManager.savePendingVerifier(verifier, challenge)) {
                    return@withContext NetworkResult.Failure(com.nativelap.ecoguard.core.network.NetworkError.Unknown)
                }
                when (val result = remote.authorize(challenge)) {
                    is NetworkResult.Failure -> {
                        sessionManager.cancelPendingLogin()
                        result
                    }

                    is NetworkResult.Success -> {
                        val uri = runCatching { android.net.Uri.parse(result.value.authorizationUrl) }.getOrNull()
                        if (
                            uri?.scheme != "https" ||
                            uri.host !in ALLOWED_AUTH_HOSTS ||
                            uri.userInfo != null ||
                            (uri.port != -1 && uri.port != 443) ||
                            uri.fragment != null
                        ) {
                            sessionManager.cancelPendingLogin()
                            NetworkResult.Failure(com.nativelap.ecoguard.core.network.NetworkError.InvalidResponse)
                        } else {
                            NetworkResult.Success(uri.toString())
                        }
                    }
                }
            }

        override suspend fun completeLogin(
            handoffCode: String,
            challenge: String,
        ): NetworkResult<Unit> =
            withContext(Dispatchers.IO) {
                if (handoffCode.isBlank() || handoffCode.length > MAX_HANDOFF_CODE_LENGTH) {
                    return@withContext NetworkResult.Failure(
                        com.nativelap.ecoguard.core.network.NetworkError.InvalidResponse,
                    )
                }
                exchangeMutex.withLock {
                    val verifier =
                        sessionManager.claimPendingExchange(challenge)
                            ?: return@withLock NetworkResult.Failure(
                                com.nativelap.ecoguard.core.network.NetworkError.InvalidResponse,
                            )
                    when (val result = remote.exchange(handoffCode, verifier)) {
                        is NetworkResult.Failure -> {
                            result
                        }

                        is NetworkResult.Success -> {
                            val response = result.value
                            if (response.accessToken.isBlank() || response.refreshToken.isBlank()) {
                                NetworkResult.Failure(com.nativelap.ecoguard.core.network.NetworkError.InvalidResponse)
                            } else {
                                val session = response.toDomain(UUID.randomUUID().toString())
                                if (sessionManager.saveSessionForPendingExchange(verifier, session)) {
                                    NetworkResult.Success(Unit)
                                } else {
                                    NetworkResult.Failure(com.nativelap.ecoguard.core.network.NetworkError.Unknown)
                                }
                            }
                        }
                    }
                }
            }

        override fun cancelLogin() {
            sessionManager.cancelPendingLogin()
        }

        override suspend fun matchesPendingAttempt(challenge: String): Boolean =
            sessionManager.matchesPendingChallenge(challenge)

        private fun randomVerifier(): String {
            val randomBytes = ByteArray(32).also(SecureRandom()::nextBytes)
            return Base64.encodeToString(
                randomBytes,
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING,
            )
        }

        private companion object {
            const val MAX_HANDOFF_CODE_LENGTH = 2048
            val ALLOWED_AUTH_HOSTS = setOf("oauth.authorization.datagsm.kr")
        }
    }
