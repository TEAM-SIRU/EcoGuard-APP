package com.nativelap.ecoguard.feature.auth.data

import com.nativelap.ecoguard.core.network.auth.SessionTokenAccess
import com.nativelap.ecoguard.feature.auth.domain.AuthSession
import com.nativelap.ecoguard.feature.auth.domain.AuthUser
import com.nativelap.ecoguard.feature.auth.domain.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val PENDING_VERIFIER_TTL_MILLIS = 10 * 60 * 1000L

@Singleton
class SessionManager
    @Inject
    constructor(
        private val store: EncryptedAuthStore,
        private val json: Json,
    ) : SessionTokenAccess,
        SessionRepository {
        private val lock = Any()
        private var currentSession = readSession()
        private var pendingAttempt: PendingAuthAttempt? = null
        private val pendingLogoutAccessTokens = linkedSetOf<String>()
        private val _session = MutableStateFlow(currentSession)
        override val session: StateFlow<AuthSession?> = _session.asStateFlow()

        override fun accessToken(): String? = getAccessToken()

        override fun clearLocalSession(): Boolean = expireSession()

        override fun pendingLogoutAccessTokens(): List<String> =
            synchronized(lock) { pendingLogoutAccessTokens.toList() }

        override fun rememberPendingLogout(accessToken: String) {
            synchronized(lock) { pendingLogoutAccessTokens.add(accessToken) }
        }

        override fun clearPendingLogout(accessToken: String) {
            synchronized(lock) { pendingLogoutAccessTokens.remove(accessToken) }
        }

        override fun getAccessToken(): String? = synchronized(lock) { currentSession?.accessToken }

        override fun getRefreshToken(): String? = synchronized(lock) { currentSession?.refreshToken }

        override fun updateTokens(
            expectedRefreshToken: String,
            accessToken: String,
            refreshToken: String,
        ): Boolean =
            synchronized(lock) {
                val current = currentSession ?: return false
                if (current.refreshToken != expectedRefreshToken) return false
                val stored = readStored() ?: return false
                val written =
                    store.write(
                        json.encodeToString(
                            stored.copy(
                                session = stored.session?.copy(accessToken = accessToken, refreshToken = refreshToken),
                            ),
                        ),
                    )
                if (!written) return false
                currentSession = current.copy(accessToken = accessToken, refreshToken = refreshToken)
                true
            }

        override fun expireSession(expectedRefreshToken: String?): Boolean =
            synchronized(lock) {
                if (expectedRefreshToken != null && currentSession?.refreshToken != expectedRefreshToken) return true
                val cleared = store.clear()
                pendingAttempt = null
                currentSession = null
                _session.value = null
                cleared
            }

        fun saveSession(session: AuthSession): Boolean =
            synchronized(lock) {
                val written = store.write(json.encodeToString(StoredAuthState(session = session.toStoredSession())))
                if (!written) return false
                currentSession = session
                _session.value = session
                true
            }

        fun savePendingVerifier(
            verifier: String,
            challenge: String,
        ): Boolean =
            synchronized(lock) {
                if (verifier.isBlank() || challenge.isBlank()) return false
                pendingAttempt = PendingAuthAttempt(verifier, challenge, System.currentTimeMillis())
                true
            }

        fun cancelPendingLogin() =
            synchronized(lock) {
                pendingAttempt = null
            }

        fun matchesPendingChallenge(challenge: String): Boolean =
            synchronized(lock) {
                val current = pendingAttempt ?: return false
                if (current.isExpired()) {
                    pendingAttempt = null
                    return false
                }
                challenge.isNotBlank() && current.challenge == challenge
            }

        fun claimPendingExchange(challenge: String): String? =
            synchronized(lock) {
                val current = pendingAttempt ?: return null
                if (current.isExpired()) {
                    pendingAttempt = null
                    return null
                }
                if (challenge.isBlank() || current.challenge != challenge || current.exchangeAttemptStarted) {
                    return null
                }
                pendingAttempt = current.copy(exchangeAttemptStarted = true)
                current.verifier
            }

        fun saveSessionForPendingExchange(
            expectedVerifier: String,
            session: AuthSession,
        ): Boolean =
            synchronized(lock) {
                val attempt = pendingAttempt ?: return false
                if (attempt.isExpired() || attempt.verifier != expectedVerifier || !attempt.exchangeAttemptStarted) {
                    return false
                }
                val stored = readStored() ?: StoredAuthState()
                val nextState = stored.copy(session = session.toStoredSession())
                if (!store.write(json.encodeToString(nextState))) return false
                pendingAttempt = null
                currentSession = session
                _session.value = session
                true
            }

        private fun readSession(): AuthSession? = readTokens()

        private fun readTokens(): AuthSession? = readStored()?.session?.toDomain()

        private fun PendingAuthAttempt.isExpired(): Boolean {
            val ageMillis = System.currentTimeMillis() - createdAtMillis
            return ageMillis !in 0..PENDING_VERIFIER_TTL_MILLIS
        }

        private fun readStored(): StoredAuthState? =
            store.read()?.let {
                runCatching { json.decodeFromString<StoredAuthState>(it) }.getOrNull()
            }

        @kotlinx.serialization.Serializable
        private data class StoredAuthState(
            val session: StoredSession? = null,
        )

        private data class PendingAuthAttempt(
            val verifier: String,
            val challenge: String,
            val createdAtMillis: Long,
            val exchangeAttemptStarted: Boolean = false,
        )

        @kotlinx.serialization.Serializable
        private data class StoredSession(
            val sessionId: String,
            val accessToken: String,
            val refreshToken: String,
            val user: AuthUserDto?,
        ) {
            fun toDomain() =
                AuthSession(
                    sessionId,
                    accessToken,
                    refreshToken,
                    user?.let { AuthUser(it.userId, it.name, it.role, it.studentNumber, it.grade, it.classNo) },
                )
        }

        private fun AuthSession.toStoredSession() =
            StoredSession(
                sessionId,
                accessToken,
                refreshToken,
                user?.let { AuthUserDto(it.userId, it.name, it.role, it.studentNumber, it.grade, it.classNo) },
            )
    }
