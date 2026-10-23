package com.nativelap.ecoguard.feature.auth.domain

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile
import com.nativelap.ecoguard.feature.user.module.domain.repository.UserRepository
import com.nativelap.ecoguard.feature.user.module.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignOutUseCaseTest {
    @Test
    fun `retains retryable token and reports retry CTA state`() =
        runBlocking {
            val sessionRepository = FakeSessionRepository(accessToken = "current")
            val logoutRepository = FakeUserRepository { AppResult.Failure(AppError.Connectivity) }

            val result =
                SignOutUseCase(sessionRepository, LogoutUseCase(logoutRepository))()

            assertTrue(result.localSessionCleared)
            assertTrue(result.serverLogoutFailed)
            assertTrue(result.hasRetryableLogoutPending)
            assertEquals(listOf("current"), sessionRepository.pendingLogoutAccessTokens())
            assertEquals(listOf("current"), logoutRepository.requestedTokens)
        }

    @Test
    fun `removes terminal failure token and does not report retry CTA state`() =
        runBlocking {
            val sessionRepository = FakeSessionRepository(accessToken = "current")
            val logoutRepository = FakeUserRepository { AppResult.Failure(AppError.Http(401)) }

            val result =
                SignOutUseCase(sessionRepository, LogoutUseCase(logoutRepository))()

            assertTrue(result.localSessionCleared)
            assertTrue(result.serverLogoutFailed)
            assertFalse(result.hasRetryableLogoutPending)
            assertTrue(sessionRepository.pendingLogoutAccessTokens().isEmpty())
            assertEquals(listOf("current"), logoutRepository.requestedTokens)
        }

    private class FakeSessionRepository(
        private val accessToken: String?,
    ) : SessionRepository {
        private val pendingTokens = linkedSetOf<String>()
        override val session = MutableStateFlow<AuthSession?>(null)

        override fun accessToken(): String? = accessToken

        override fun clearLocalSession(): Boolean = true

        override fun pendingLogoutAccessTokens(): List<String> = pendingTokens.toList()

        override fun rememberPendingLogout(accessToken: String) {
            pendingTokens.add(accessToken)
        }

        override fun clearPendingLogout(accessToken: String) {
            pendingTokens.remove(accessToken)
        }
    }

    private class FakeUserRepository(
        private val logoutResult: (String) -> AppResult<Unit>,
    ) : UserRepository {
        val requestedTokens = mutableListOf<String>()

        override suspend fun getMyProfile(): AppResult<UserProfile> = error("Not used in this test")

        override suspend fun withdraw(): AppResult<Unit> = error("Not used in this test")

        override suspend fun logout(accessToken: String): AppResult<Unit> {
            requestedTokens.add(accessToken)
            return logoutResult(accessToken)
        }
    }
}
