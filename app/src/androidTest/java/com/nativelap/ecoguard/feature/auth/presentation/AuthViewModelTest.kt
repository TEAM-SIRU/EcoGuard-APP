package com.nativelap.ecoguard.feature.auth.presentation

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkError
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.auth.domain.AuthRepository
import com.nativelap.ecoguard.feature.auth.domain.AuthSession
import com.nativelap.ecoguard.feature.auth.domain.CancelAuthUseCase
import com.nativelap.ecoguard.feature.auth.domain.ClearSessionAfterWithdrawalUseCase
import com.nativelap.ecoguard.feature.auth.domain.CompleteAuthUseCase
import com.nativelap.ecoguard.feature.auth.domain.ObserveSessionUseCase
import com.nativelap.ecoguard.feature.auth.domain.SessionRepository
import com.nativelap.ecoguard.feature.auth.domain.SignOutUseCase
import com.nativelap.ecoguard.feature.auth.domain.StartAuthUseCase
import com.nativelap.ecoguard.feature.auth.domain.VerifyAuthAttemptUseCase
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile
import com.nativelap.ecoguard.feature.user.module.domain.repository.UserRepository
import com.nativelap.ecoguard.feature.user.module.domain.usecase.LogoutUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthViewModelTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var repository: FakeAuthRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var viewModel: AuthViewModel
    private val viewModelStore = ViewModelStore()

    @Before
    fun setUp() {
        composeRule.setContent {}
        repository = FakeAuthRepository()
        userRepository = FakeUserRepository()
        viewModel =
            AuthViewModel(
                ObserveSessionUseCase(repository),
                StartAuthUseCase(repository),
                CompleteAuthUseCase(repository),
                VerifyAuthAttemptUseCase(repository),
                CancelAuthUseCase(repository),
                SignOutUseCase(repository, LogoutUseCase(userRepository)),
                ClearSessionAfterWithdrawalUseCase(repository),
            ).also { viewModelStore.put("auth-view-model", it) }
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
    }

    @Test
    fun startLoginFailureEndsLoadingAndShowsTheFailureState() {
        repository.startResult = NetworkResult.Failure(NetworkError.Connectivity)

        composeRule.runOnIdle { viewModel.startLogin() }
        waitUntil { viewModel.uiState.value.hasLoginFailed }

        assertFalse(viewModel.uiState.value.isLoggingIn)
        assertEquals(null, viewModel.uiState.value.authorizationUrl)
    }

    @Test
    fun accountWithdrawalClearsLocalSessionAndPendingLogoutTokensWithoutCallingLogout() {
        repository.session.value = AuthSession("session", "access", "refresh", null)
        repository.rememberPendingLogout("stale-access")

        viewModel.onWithdrawalSucceeded()

        assertEquals(null, repository.session.value)
        assertTrue(repository.pendingLogoutAccessTokens().isEmpty())
        assertTrue(userRepository.logoutTokens.isEmpty())
        assertFalse(viewModel.uiState.value.hasSessionClearFailed)
    }

    @Test
    fun repeatedStartLoginWhileAuthorizationIsPendingIsIgnored() {
        composeRule.runOnIdle {
            viewModel.startLogin()
            viewModel.startLogin()
        }
        waitUntil { viewModel.uiState.value.authorizationUrl != null }

        assertEquals(1, repository.startCalls)
    }

    @Test
    fun failedToOpenAuthorizationUrlCancelsThePendingLogin() {
        startAuthorization()

        composeRule.runOnIdle { viewModel.onAuthorizationUrlHandled(false) }

        assertTrue(repository.cancelCount == 1)
        assertTrue(viewModel.uiState.value.hasLoginFailed)
        assertFalse(viewModel.uiState.value.isLoggingIn)
    }

    @Test
    fun externalAuthReturnWithoutSessionShowsFailure() {
        startAuthorization()

        composeRule.runOnIdle {
            viewModel.onAuthorizationUrlHandled(true)
            viewModel.onExternalAuthReturned()
        }

        assertTrue(viewModel.uiState.value.hasLoginFailed)
        assertFalse(viewModel.uiState.value.isLoggingIn)
    }

    @Test
    fun validHandoffCallbackExchangesCodeAndPublishesTheSession() {
        startAuthorization()
        var wasHandled = false

        composeRule.runOnIdle {
            wasHandled = viewModel.handleHandoffLink(validHandoffLink())
        }
        waitUntil { !viewModel.uiState.value.isLoggingIn && viewModel.uiState.value.sessionId != null }

        assertTrue(wasHandled)
        assertFalse(viewModel.uiState.value.hasLoginFailed)
        assertEquals("test-session", viewModel.uiState.value.sessionId)
        assertEquals(1, repository.exchangeCount)
        assertEquals("one-time-code", repository.lastHandoffCode)
    }

    @Test
    fun handoffCallbackRejectsUntrustedUrlsAndDuplicateOrUnknownParameters() {
        val invalidLinks =
            listOf(
                "http://ecoguard.https.gsmsv.site/auth/mobile-callback?handoff_code=code&handoff_challenge=challenge",
                "https://evil.example/auth/mobile-callback?handoff_code=code&handoff_challenge=challenge",
                "https://ecoguard.https.gsmsv.site:444/auth/mobile-callback?handoff_code=code&handoff_challenge=challenge",
                "https://user@ecoguard.https.gsmsv.site/auth/mobile-callback?handoff_code=code&handoff_challenge=challenge",
                "https://ecoguard.https.gsmsv.site/auth/other?handoff_code=code&handoff_challenge=challenge",
                "https://ecoguard.https.gsmsv.site/auth/mobile-callback?handoff_code=code&handoff_challenge=challenge#fragment",
                "https://ecoguard.https.gsmsv.site/auth/mobile-callback?handoff_code=code&handoff_challenge=challenge&extra=value",
                "https://ecoguard.https.gsmsv.site/auth/mobile-callback?handoff_code=first&handoff_code=second&handoff_challenge=challenge",
                "https://ecoguard.https.gsmsv.site/auth/mobile-callback?handoff_code=code&handoff_challenge=first&handoff_challenge=second",
            )

        invalidLinks.forEach { callbackUrl ->
            var wasHandled = true
            composeRule.runOnIdle {
                wasHandled = viewModel.handleHandoffLink(Uri.parse(callbackUrl))
            }
            assertFalse(callbackUrl, wasHandled)
        }
        assertEquals(0, repository.exchangeCount)
    }

    @Test
    fun callbackRejectsMismatchedChallengeAndProviderErrors() {
        startAuthorization()
        var wasHandled = false

        composeRule.runOnIdle {
            wasHandled =
                viewModel.handleHandoffLink(
                    Uri.parse(
                        "$APP_LINK?handoff_code=one-time-code&handoff_challenge=different-challenge",
                    ),
                )
        }
        waitUntil { viewModel.uiState.value.hasLoginFailed }

        assertTrue(wasHandled)
        assertEquals(0, repository.exchangeCount)

        startAuthorization()
        composeRule.runOnIdle {
            wasHandled =
                viewModel.handleHandoffLink(
                    Uri.parse(
                        "$APP_LINK?handoff_code=one-time-code&handoff_challenge=test-challenge&error=access_denied",
                    ),
                )
        }
        waitUntil { viewModel.uiState.value.hasLoginFailed }

        assertTrue(wasHandled)
        assertEquals(0, repository.exchangeCount)
    }

    @Test
    fun callbackWithoutCodeShowsFailureWithoutExchanging() {
        startAuthorization()
        var wasHandled = false

        composeRule.runOnIdle {
            wasHandled =
                viewModel.handleHandoffLink(
                    Uri.parse("$APP_LINK?handoff_challenge=test-challenge"),
                )
        }
        waitUntil { viewModel.uiState.value.hasLoginFailed }

        assertTrue(wasHandled)
        assertEquals(0, repository.exchangeCount)
    }

    @Test
    fun signOutClearsLocalSessionAndKeepsRetryableServerFailure() {
        repository.session.value = AuthSession("test-session", "access-token", "refresh-token", null)
        userRepository.logoutResult = AppResult.Failure(AppError.Connectivity)

        composeRule.runOnIdle { viewModel.signOut() }
        waitUntil { !viewModel.uiState.value.isLoggingIn && viewModel.uiState.value.hasServerLogoutFailed }

        assertEquals(null, viewModel.uiState.value.sessionId)
        assertTrue(viewModel.uiState.value.hasRetryableLogoutPending)
        assertEquals(listOf("access-token"), userRepository.logoutTokens)
    }

    @Test
    fun nextLoginRetriesPendingServerLogoutBeforeStartingAuthorization() {
        repository.session.value = AuthSession("test-session", "access-token", "refresh-token", null)
        userRepository.logoutResult = AppResult.Failure(AppError.Connectivity)

        composeRule.runOnIdle { viewModel.signOut() }
        waitUntil { !viewModel.uiState.value.isLoggingIn && viewModel.uiState.value.hasRetryableLogoutPending }
        userRepository.logoutResult = AppResult.Success(Unit)

        composeRule.runOnIdle { viewModel.startLogin() }
        waitUntil { viewModel.uiState.value.authorizationUrl != null }

        assertEquals(listOf("access-token", "access-token"), userRepository.logoutTokens)
        assertFalse(viewModel.uiState.value.hasRetryableLogoutPending)
        assertEquals(1, repository.startCalls)
    }

    @Test
    fun repeatedSignOutWhileLogoutIsPendingIsIgnored() {
        repository.session.value = AuthSession("test-session", "access-token", "refresh-token", null)
        val logoutCompletion = CompletableDeferred<AppResult<Unit>>()
        userRepository.logoutCompletion = logoutCompletion

        composeRule.runOnIdle {
            viewModel.signOut()
            viewModel.signOut()
        }
        waitUntil { viewModel.uiState.value.isLoggingIn }
        assertEquals(1, userRepository.logoutTokens.size)

        logoutCompletion.complete(AppResult.Success(Unit))
        waitUntil { !viewModel.uiState.value.isLoggingIn && viewModel.uiState.value.sessionId == null }

        assertEquals(listOf("access-token"), userRepository.logoutTokens)
    }

    private fun startAuthorization() {
        composeRule.runOnIdle { viewModel.startLogin() }
        waitUntil { viewModel.uiState.value.authorizationUrl != null }
        composeRule.runOnIdle { viewModel.onAuthorizationUrlHandled(true) }
    }

    private fun validHandoffLink(): Uri =
        Uri.parse("$APP_LINK?handoff_code=one-time-code&handoff_challenge=test-challenge")

    private fun waitUntil(condition: () -> Boolean) {
        composeRule.waitUntil(timeoutMillis = 5_000, condition = condition)
    }

    private class FakeAuthRepository :
        AuthRepository,
        SessionRepository {
        override val session = MutableStateFlow<AuthSession?>(null)
        var startResult: NetworkResult<String> =
            NetworkResult.Success("https://oauth.authorization.datagsm.kr/authorize")
        var completeResult: NetworkResult<Unit> = NetworkResult.Success(Unit)
        var cancelCount = 0
        var startCalls = 0
        var exchangeCount = 0
        var lastHandoffCode: String? = null
        private var pendingChallenge: String? = null
        private val retryableLogoutTokens = linkedSetOf<String>()

        override suspend fun startLogin(): NetworkResult<String> {
            startCalls += 1
            if (startResult is NetworkResult.Success) pendingChallenge = "test-challenge"
            return startResult
        }

        override suspend fun completeLogin(
            handoffCode: String,
            challenge: String,
        ): NetworkResult<Unit> {
            exchangeCount += 1
            lastHandoffCode = handoffCode
            if (completeResult is NetworkResult.Success) {
                session.value = AuthSession("test-session", "access", "refresh", null)
            }
            return completeResult
        }

        override suspend fun matchesPendingAttempt(challenge: String): Boolean = pendingChallenge == challenge

        override fun cancelLogin() {
            cancelCount += 1
            pendingChallenge = null
        }

        override fun accessToken(): String? = session.value?.accessToken

        override fun clearLocalSession(): Boolean {
            session.value = null
            return true
        }

        override fun pendingLogoutAccessTokens(): List<String> = retryableLogoutTokens.toList()

        override fun rememberPendingLogout(accessToken: String) {
            retryableLogoutTokens.add(accessToken)
        }

        override fun clearPendingLogout(accessToken: String) {
            retryableLogoutTokens.remove(accessToken)
        }
    }

    private class FakeUserRepository : UserRepository {
        var logoutResult: AppResult<Unit> = AppResult.Success(Unit)
        var logoutCompletion: CompletableDeferred<AppResult<Unit>>? = null
        val logoutTokens = mutableListOf<String>()

        override suspend fun getMyProfile(): AppResult<UserProfile> = error("Unused")

        override suspend fun withdraw(): AppResult<Unit> = error("Unused")

        override suspend fun logout(accessToken: String): AppResult<Unit> {
            logoutTokens += accessToken
            return logoutCompletion?.await() ?: logoutResult
        }
    }

    private companion object {
        const val APP_LINK = "https://ecoguard.https.gsmsv.site/auth/mobile-callback"
    }
}
