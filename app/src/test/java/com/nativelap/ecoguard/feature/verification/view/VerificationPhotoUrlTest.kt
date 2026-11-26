package com.nativelap.ecoguard.feature.verification.view

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VerificationPhotoUrlTest {
    private val apiOrigin = "https://ecoguard.https.gsmsv.site/"

    @Test
    fun `resolves a files path against the API origin`() {
        assertEquals(
            "https://ecoguard.https.gsmsv.site/files/verification/photo.jpg",
            resolveVerificationPhotoUrl("/files/verification/photo.jpg", apiOrigin),
        )
    }

    @Test
    fun `rejects missing non files and absolute paths`() {
        listOf<String?>(
            null,
            "",
            "   ",
            "/uploads/photo.jpg",
            "https://ecoguard.https.gsmsv.site/files/photo.jpg",
            "https://evil.example/files/photo.jpg",
            "//evil.example/files/photo.jpg",
        ).forEach { path ->
            assertNull("Expected rejected path: $path", resolveVerificationPhotoUrl(path, apiOrigin))
        }
    }

    @Test
    fun `rejects traversal backslashes and ambiguous path encodings`() {
        listOf(
            "/files/../private/photo.jpg",
            "/files/%2e%2e/private/photo.jpg",
            "/files/%252e%252e/private/photo.jpg",
            "/files\\private\\photo.jpg",
            "/files/%2f%2fevil.example/photo.jpg",
            "/files/photo.jpg?redirect=https://evil.example",
            "/files/photo.jpg#fragment",
        ).forEach { path ->
            assertNull("Expected rejected path: $path", resolveVerificationPhotoUrl(path, apiOrigin))
        }
    }

    @Test
    fun `rejects a non https or invalid API origin`() {
        assertNull(resolveVerificationPhotoUrl("/files/photo.jpg", "http://ecoguard.https.gsmsv.site/"))
        assertNull(resolveVerificationPhotoUrl("/files/photo.jpg", "https://user:info@ecoguard.https.gsmsv.site/"))
    }
}
