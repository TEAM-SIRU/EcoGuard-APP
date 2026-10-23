package com.nativelap.ecoguard.feature.auth.domain

import com.nativelap.ecoguard.core.domain.result.AppError
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LogoutRetryPolicyTest {
    @Test
    fun `retries transient errors`() {
        assertTrue(shouldRetryLogout(AppError.Connectivity))
        assertTrue(shouldRetryLogout(AppError.Unknown))
        assertTrue(shouldRetryLogout(AppError.Http(408)))
        assertTrue(shouldRetryLogout(AppError.Http(429)))
        assertTrue(shouldRetryLogout(AppError.Http(500)))
    }

    @Test
    fun `does not retry permanent errors`() {
        assertFalse(shouldRetryLogout(AppError.Http(400)))
        assertFalse(shouldRetryLogout(AppError.Http(401)))
        assertFalse(shouldRetryLogout(AppError.Http(403)))
        assertFalse(shouldRetryLogout(AppError.InvalidResponse))
    }
}
