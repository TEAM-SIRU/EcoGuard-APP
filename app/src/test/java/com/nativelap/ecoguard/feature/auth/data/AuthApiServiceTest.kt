package com.nativelap.ecoguard.feature.auth.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.http.Body
import retrofit2.http.POST

class AuthApiServiceTest {
    @Test
    fun `authorize and login use server contract paths and request bodies`() {
        val authorize = method("authorize")
        val login = method("login")

        assertEquals("api/v1/auth/authorize", authorize.getAnnotation(POST::class.java).value)
        assertEquals("api/v1/auth/login", login.getAnnotation(POST::class.java).value)
        assertNotNull(
            authorize.parameterAnnotations
                .flatMap { it.toList() }
                .filterIsInstance<Body>()
                .single(),
        )
        assertNotNull(
            login.parameterAnnotations
                .flatMap { it.toList() }
                .filterIsInstance<Body>()
                .single(),
        )
    }

    @Test
    fun `auth DTOs preserve explicit JSON field names`() {
        val json = Json { encodeDefaults = false }

        assertEquals(
            """{"handoffChallenge":"challenge","challengeMethod":"S256"}""",
            json.encodeToString(AuthorizeRequestDto("challenge", "S256")),
        )
        assertEquals(
            """{"authCode":"one-time-code","redirectUri":"https://ecoguard.https.gsmsv.site/auth/mobile-callback"}""",
            json.encodeToString(
                LoginRequestDto(
                    "one-time-code",
                    "https://ecoguard.https.gsmsv.site/auth/mobile-callback",
                ),
            ),
        )
    }

    @Test
    fun `session response maps into a uniquely identified domain session`() {
        val dto =
            Json.decodeFromString<AuthSessionResponseDto>(
                """{"accessToken":"access","refreshToken":"refresh","user":{"userId":17,"name":"Siru"}}""",
            )
        val session = dto.toDomain("session-unique-id")

        assertEquals("access", session.accessToken)
        assertEquals("refresh", session.refreshToken)
        assertEquals("session-unique-id", session.sessionId)
        assertEquals(17L, session.user?.userId)
        assertEquals("Siru", session.user?.name)
    }

    private fun method(name: String) = AuthApiService::class.java.methods.single { it.name == name }
}
