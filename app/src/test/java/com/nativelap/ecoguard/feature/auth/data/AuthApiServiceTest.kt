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
    fun `authorize and exchange use proposed server contract paths and request bodies`() {
        val authorize = method("authorize")
        val exchange = method("exchange")

        assertEquals("api/v1/auth/authorize", authorize.getAnnotation(POST::class.java).value)
        assertEquals("api/v1/auth/exchange", exchange.getAnnotation(POST::class.java).value)
        assertNotNull(
            authorize.parameterAnnotations
                .flatMap { it.toList() }
                .filterIsInstance<Body>()
                .single(),
        )
        assertNotNull(
            exchange.parameterAnnotations
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
            """{"handoffCode":"one-time-code","handoffVerifier":"verifier"}""",
            json.encodeToString(ExchangeRequestDto("one-time-code", "verifier")),
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
