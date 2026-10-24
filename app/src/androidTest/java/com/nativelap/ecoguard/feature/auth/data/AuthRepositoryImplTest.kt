package com.nativelap.ecoguard.feature.auth.data

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.util.Base64
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.ecoguard.core.network.NetworkError
import com.nativelap.ecoguard.core.network.NetworkResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.security.KeyStore
import java.security.MessageDigest

class AuthRepositoryImplTest {
    @Test
    fun startLoginSendsS256ChallengeAndAcceptsTheConfiguredProvider() =
        runBlocking {
            val remote = FakeAuthRemoteDataSource()
            val harness = createHarness(remote)

            try {
                val result = harness.repository.startLogin()

                assertTrue(result is NetworkResult.Success)
                assertEquals(
                    "https://oauth.authorization.datagsm.kr/authorize",
                    (result as NetworkResult.Success).value,
                )

                val challenge = checkNotNull(remote.authorizeChallenge)
                val verifier = checkNotNull(harness.sessionManager.claimPendingExchange(challenge))
                val derivedChallenge =
                    Base64.encodeToString(
                        MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray(Charsets.US_ASCII)),
                        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING,
                    )

                assertEquals(challenge, derivedChallenge)
                assertEquals(43, verifier.length)
            } finally {
                harness.clear()
            }
        }

    @Test
    fun startLoginRejectsUntrustedAuthorizationUrlsAndClearsThePendingAttempt() =
        runBlocking {
            val invalidUrls =
                listOf(
                    "http://oauth.authorization.datagsm.kr/authorize",
                    "https://evil.example/authorize",
                    "https://user@oauth.authorization.datagsm.kr/authorize",
                    "https://oauth.authorization.datagsm.kr:444/authorize",
                    "https://oauth.authorization.datagsm.kr/authorize#fragment",
                )

            invalidUrls.forEach { authorizationUrl ->
                val remote = FakeAuthRemoteDataSource(authorizationUrl = authorizationUrl)
                val harness = createHarness(remote)

                try {
                    val result = harness.repository.startLogin()

                    assertEquals(NetworkResult.Failure(NetworkError.InvalidResponse), result)
                    assertFalse(harness.sessionManager.matchesPendingChallenge(checkNotNull(remote.authorizeChallenge)))
                } finally {
                    harness.clear()
                }
            }
        }

    @Test
    fun authorizeFailureCancelsThePendingAttempt() =
        runBlocking {
            val remote =
                FakeAuthRemoteDataSource().apply {
                    authorizeResult = NetworkResult.Failure(NetworkError.Connectivity)
                }
            val harness = createHarness(remote)

            try {
                assertEquals(NetworkResult.Failure(NetworkError.Connectivity), harness.repository.startLogin())
                assertFalse(harness.sessionManager.matchesPendingChallenge(checkNotNull(remote.authorizeChallenge)))
            } finally {
                harness.clear()
            }
        }

    @Test
    fun completeLoginPersistsTheExchangedSessionAndRejectsASecondExchange() =
        runBlocking {
            val remote =
                FakeAuthRemoteDataSource().apply {
                    exchangeResult =
                        NetworkResult.Success(
                            AuthSessionResponseDto(
                                accessToken = "access-token",
                                refreshToken = "refresh-token",
                                user = AuthUserDto(userId = 17L, name = "Siru"),
                            ),
                        )
                }
            val harness = createHarness(remote)

            try {
                assertTrue(harness.repository.startLogin() is NetworkResult.Success)
                val challenge = checkNotNull(remote.authorizeChallenge)

                assertTrue(harness.repository.matchesPendingAttempt(challenge))
                assertEquals(NetworkResult.Success(Unit), harness.repository.completeLogin("one-time-code", challenge))
                assertEquals("access-token", harness.sessionManager.getAccessToken())
                assertEquals("refresh-token", harness.sessionManager.getRefreshToken())
                val restoredSessionManager = SessionManager(harness.store, Json { ignoreUnknownKeys = true })
                assertEquals("access-token", restoredSessionManager.getAccessToken())
                assertEquals("refresh-token", restoredSessionManager.getRefreshToken())
                assertEquals(
                    17L,
                    restoredSessionManager.session.value
                        ?.user
                        ?.userId,
                )
                assertEquals(
                    "Siru",
                    restoredSessionManager.session.value
                        ?.user
                        ?.name,
                )
                assertEquals(1, remote.exchangeCalls.size)
                assertEquals("one-time-code", remote.exchangeCalls.single().first)
                assertEquals(
                    NetworkResult.Failure(NetworkError.InvalidResponse),
                    harness.repository.completeLogin("one-time-code", challenge),
                )
                assertEquals(1, remote.exchangeCalls.size)
            } finally {
                harness.clear()
            }
        }

    @Test
    fun completeLoginRejectsBlankTokensWithoutPersistingASession() =
        runBlocking {
            listOf(
                " " to "refresh-token",
                "access-token" to " ",
            ).forEach { (accessToken, refreshToken) ->
                val remote =
                    FakeAuthRemoteDataSource().apply {
                        exchangeResult =
                            NetworkResult.Success(
                                AuthSessionResponseDto(
                                    accessToken = accessToken,
                                    refreshToken = refreshToken,
                                    user = AuthUserDto(userId = 17L, name = "Siru"),
                                ),
                            )
                    }
                val harness = createHarness(remote)

                try {
                    assertTrue(harness.repository.startLogin() is NetworkResult.Success)
                    val challenge = checkNotNull(remote.authorizeChallenge)

                    assertEquals(
                        NetworkResult.Failure(NetworkError.InvalidResponse),
                        harness.repository.completeLogin("one-time-code", challenge),
                    )
                    assertEquals(null, harness.sessionManager.getAccessToken())
                    assertEquals(null, harness.sessionManager.getRefreshToken())
                    assertEquals(1, remote.exchangeCalls.size)
                } finally {
                    harness.clear()
                }
            }
        }

    @Test
    fun sessionPersistenceFailureDoesNotPublishOrPersistTheSession() =
        runBlocking {
            val remote =
                FakeAuthRemoteDataSource().apply {
                    exchangeResult =
                        NetworkResult.Success(
                            AuthSessionResponseDto(
                                accessToken = "access-token",
                                refreshToken = "refresh-token",
                                user = AuthUserDto(userId = 17L, name = "Siru"),
                            ),
                        )
                }
            val harness = createHarness(remote, failSessionWrites = true)

            try {
                assertTrue(harness.repository.startLogin() is NetworkResult.Success)
                val challenge = checkNotNull(remote.authorizeChallenge)

                assertEquals(
                    NetworkResult.Failure(NetworkError.Unknown),
                    harness.repository.completeLogin("one-time-code", challenge),
                )
                assertEquals(null, harness.sessionManager.getAccessToken())
                assertEquals(null, harness.sessionManager.getRefreshToken())
                assertEquals(1, remote.exchangeCalls.size)
            } finally {
                harness.clear()
            }
        }

    @Test
    fun completeLoginRejectsInvalidCodesAndAChallengeMismatchBeforeCallingTheServer() =
        runBlocking {
            val remote = FakeAuthRemoteDataSource()
            val harness = createHarness(remote)

            try {
                assertTrue(harness.repository.startLogin() is NetworkResult.Success)
                val challenge = checkNotNull(remote.authorizeChallenge)

                assertEquals(
                    NetworkResult.Failure(NetworkError.InvalidResponse),
                    harness.repository.completeLogin(" ", challenge),
                )
                assertEquals(
                    NetworkResult.Failure(NetworkError.InvalidResponse),
                    harness.repository.completeLogin("x".repeat(MAX_HANDOFF_CODE_LENGTH + 1), challenge),
                )
                assertEquals(
                    NetworkResult.Failure(NetworkError.InvalidResponse),
                    harness.repository.completeLogin("one-time-code", "different-challenge"),
                )
                assertTrue(remote.exchangeCalls.isEmpty())
            } finally {
                harness.clear()
            }
        }

    @Test
    fun failedExchangeDoesNotPersistTokensAndCannotBeReplayed() =
        runBlocking {
            val remote =
                FakeAuthRemoteDataSource().apply {
                    exchangeResult = NetworkResult.Failure(NetworkError.Http(503))
                }
            val harness = createHarness(remote)

            try {
                assertTrue(harness.repository.startLogin() is NetworkResult.Success)
                val challenge = checkNotNull(remote.authorizeChallenge)

                assertEquals(
                    NetworkResult.Failure(NetworkError.Http(503)),
                    harness.repository.completeLogin("one-time-code", challenge),
                )
                assertEquals(null, harness.sessionManager.getAccessToken())
                assertEquals(
                    NetworkResult.Failure(NetworkError.InvalidResponse),
                    harness.repository.completeLogin("one-time-code", challenge),
                )
                assertEquals(1, remote.exchangeCalls.size)
            } finally {
                harness.clear()
            }
        }

    private fun createHarness(
        remote: FakeAuthRemoteDataSource,
        failSessionWrites: Boolean = false,
    ): AuthHarness {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val isolatedContext = IsolatedPreferencesContext(context)
        check(
            isolatedContext
                .getSharedPreferences(SESSION_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit(),
        )
        val storeContext = if (failSessionWrites) FailingPreferencesContext(isolatedContext) else isolatedContext
        val store = EncryptedAuthStore(storeContext)
        val hadExistingAuthKey = hasAuthKey()
        val sessionManager = SessionManager(store, Json { ignoreUnknownKeys = true })

        return AuthHarness(
            AuthRepositoryImpl(remote, sessionManager),
            sessionManager,
            store,
            isolatedContext,
            hadExistingAuthKey,
        )
    }

    private data class AuthHarness(
        val repository: AuthRepositoryImpl,
        val sessionManager: SessionManager,
        val store: EncryptedAuthStore,
        private val isolatedContext: Context,
        private val hadExistingAuthKey: Boolean,
    ) {
        fun clear() {
            repository.cancelLogin()
            check(
                isolatedContext
                    .getSharedPreferences(SESSION_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .commit(),
            )
            if (!hadExistingAuthKey) {
                runCatching {
                    KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER).apply { load(null) }.deleteEntry(AUTH_KEY_ALIAS)
                }
            }
        }
    }

    private fun hasAuthKey(): Boolean =
        KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER).apply { load(null) }.containsAlias(AUTH_KEY_ALIAS)

    private class IsolatedPreferencesContext(
        context: Context,
    ) : ContextWrapper(context) {
        override fun getSharedPreferences(
            name: String,
            mode: Int,
        ): SharedPreferences = baseContext.getSharedPreferences("auth_repository_instrumentation_$name", mode)
    }

    private class FailingPreferencesContext(
        context: Context,
    ) : ContextWrapper(context) {
        override fun getSharedPreferences(
            name: String,
            mode: Int,
        ): SharedPreferences = FailingSharedPreferences
    }

    private object FailingSharedPreferences : SharedPreferences {
        override fun getAll(): MutableMap<String, *> = mutableMapOf<String, Any?>()

        override fun getString(
            key: String?,
            defValue: String?,
        ): String? = defValue

        override fun getStringSet(
            key: String?,
            defValues: MutableSet<String>?,
        ): MutableSet<String>? = defValues

        override fun getInt(
            key: String?,
            defValue: Int,
        ): Int = defValue

        override fun getLong(
            key: String?,
            defValue: Long,
        ): Long = defValue

        override fun getFloat(
            key: String?,
            defValue: Float,
        ): Float = defValue

        override fun getBoolean(
            key: String?,
            defValue: Boolean,
        ): Boolean = defValue

        override fun contains(key: String?): Boolean = false

        override fun edit(): SharedPreferences.Editor = FailingEditor

        override fun registerOnSharedPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener?,
        ) = Unit

        override fun unregisterOnSharedPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener?,
        ) = Unit

        private object FailingEditor : SharedPreferences.Editor {
            override fun putString(
                key: String?,
                value: String?,
            ): SharedPreferences.Editor = this

            override fun putStringSet(
                key: String?,
                values: MutableSet<String>?,
            ): SharedPreferences.Editor = this

            override fun putInt(
                key: String?,
                value: Int,
            ): SharedPreferences.Editor = this

            override fun putLong(
                key: String?,
                value: Long,
            ): SharedPreferences.Editor = this

            override fun putFloat(
                key: String?,
                value: Float,
            ): SharedPreferences.Editor = this

            override fun putBoolean(
                key: String?,
                value: Boolean,
            ): SharedPreferences.Editor = this

            override fun remove(key: String?): SharedPreferences.Editor = this

            override fun clear(): SharedPreferences.Editor = this

            override fun commit(): Boolean = false

            override fun apply() = Unit
        }
    }

    private class FakeAuthRemoteDataSource(
        authorizationUrl: String = "https://oauth.authorization.datagsm.kr/authorize",
    ) : AuthRemoteDataSource {
        var authorizationUrl = authorizationUrl
        var authorizeResult: NetworkResult<AuthorizeResponseDto> =
            NetworkResult.Success(AuthorizeResponseDto(authorizationUrl))
        var exchangeResult: NetworkResult<AuthSessionResponseDto> =
            NetworkResult.Failure(NetworkError.Connectivity)
        var authorizeChallenge: String? = null
        val exchangeCalls = mutableListOf<Pair<String, String>>()

        override suspend fun authorize(challenge: String): NetworkResult<AuthorizeResponseDto> {
            authorizeChallenge = challenge
            return authorizeResult
        }

        override suspend fun exchange(
            code: String,
            verifier: String,
        ): NetworkResult<AuthSessionResponseDto> {
            exchangeCalls += code to verifier
            return exchangeResult
        }
    }

    private companion object {
        const val ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore"
        const val AUTH_KEY_ALIAS = "ecoguard_auth_session_key"
        const val MAX_HANDOFF_CODE_LENGTH = 2048
        const val SESSION_PREFERENCES_NAME = "secure_auth_session"
    }
}
