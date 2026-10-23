package com.nativelap.ecoguard.core.network.auth

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response as RetrofitResponse

class TokenRefreshAuthenticatorTest {
    @Test
    fun `refreshes a rejected access token and retries once with the rotated token`() {
        val session = FakeSessionTokenAccess()
        val api = FakeRefreshApi(RetrofitResponse.success(TokenRefreshResponseDto("next-access", "next-refresh")))
        val authenticator = TokenRefreshAuthenticator(session, api)

        val request = unauthorizedRequest()
        val retry = authenticator.authenticate(null, unauthorizedResponse(request))

        assertEquals("Bearer next-access", retry?.header("Authorization"))
        assertEquals("next-refresh", session.currentRefreshToken)
        assertNull(authenticator.authenticate(null, unauthorizedResponse(retry!!, prior = true)))
    }

    @Test
    fun `expires session if refresh endpoint rejects the refresh token`() {
        val session = FakeSessionTokenAccess()
        val api = FakeRefreshApi(RetrofitResponse.error(401, okhttp3.ResponseBody.create(null, "")))
        val authenticator = TokenRefreshAuthenticator(session, api)

        assertNull(authenticator.authenticate(null, unauthorizedResponse(unauthorizedRequest())))
        assertEquals(1, session.expirationCount)
    }

    private fun unauthorizedRequest(): Request =
        Request
            .Builder()
            .url("https://ecoguard.https.gsmsv.site/api/v1/users/me")
            .header("Authorization", "Bearer old-access")
            .build()

    private fun unauthorizedResponse(
        request: Request,
        prior: Boolean = false,
    ): okhttp3.Response {
        val builder =
            okhttp3.Response
                .Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
        if (prior) {
            builder.priorResponse(
                okhttp3.Response
                    .Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(401)
                    .message("Unauthorized")
                    .build(),
            )
        }
        return builder.build()
    }

    private class FakeSessionTokenAccess : SessionTokenAccess {
        private var accessToken = "old-access"
        var currentRefreshToken = "old-refresh"
            private set
        var expirationCount = 0
            private set

        override fun getAccessToken() = accessToken

        override fun getRefreshToken() = currentRefreshToken

        override fun updateTokens(
            expectedRefreshToken: String,
            accessToken: String,
            refreshToken: String,
        ): Boolean {
            if (this.currentRefreshToken != expectedRefreshToken) return false
            this.accessToken = accessToken
            this.currentRefreshToken = refreshToken
            return true
        }

        override fun expireSession(expectedRefreshToken: String?): Boolean {
            expirationCount += 1
            return true
        }
    }

    private class FakeRefreshApi(
        private val response: RetrofitResponse<TokenRefreshResponseDto>,
    ) : com.nativelap.ecoguard.core.network.auth.TokenRefreshApi {
        override fun refresh(request: TokenRefreshRequestDto): RetrofitResponse<TokenRefreshResponseDto> = response
    }
}
