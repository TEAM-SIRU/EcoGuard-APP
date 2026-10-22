package com.nativelap.ecoguard.core.network.auth

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.lang.reflect.Proxy

class BearerTokenInterceptorTest {
    @Test
    fun `interceptor attaches bearer token when provider returns a token`() {
        val observedRequest = interceptWithToken("access-token")

        assertEquals("Bearer access-token", observedRequest.header("Authorization"))
    }

    @Test
    fun `interceptor omits authorization when token is unavailable`() {
        val observedRequest = interceptWithToken(null)

        assertNull(observedRequest.header("Authorization"))
    }

    @Test
    fun `interceptor omits blank authorization token`() {
        val observedRequest = interceptWithToken("  ")

        assertNull(observedRequest.header("Authorization"))
    }

    private fun interceptWithToken(token: String?): Request {
        val originalRequest =
            Request
                .Builder()
                .url("https://example.test/resource")
                .build()
        var proceededRequest: Request? = null
        val chain =
            Proxy.newProxyInstance(
                Interceptor.Chain::class.java.classLoader,
                arrayOf(Interceptor.Chain::class.java),
            ) { _, method, arguments ->
                when (method.name) {
                    "request" -> {
                        originalRequest
                    }

                    "proceed" -> {
                        proceededRequest = arguments?.get(0) as Request
                        Response
                            .Builder()
                            .request(proceededRequest!!)
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("OK")
                            .build()
                    }

                    else -> {
                        error("Unexpected chain method: ${method.name}")
                    }
                }
            } as Interceptor.Chain
        val tokenProvider =
            object : AccessTokenProvider {
                override fun getAccessToken(): String? = token
            }

        BearerTokenInterceptor(tokenProvider).intercept(chain)

        return checkNotNull(proceededRequest)
    }
}
