package com.nativelap.ecoguard.feature.appeal

import com.nativelap.ecoguard.feature.appeal.view.AppealPhotoTooLargeException
import com.nativelap.ecoguard.feature.appeal.view.copyBoundedTo
import com.nativelap.ecoguard.feature.appeal.view.hasImageSignature
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class AppealPhotoImportTest {
    @Test
    fun boundedCopyAcceptsContentWithinLimit() {
        val expected = byteArrayOf(1, 2, 3)
        val output = ByteArrayOutputStream()

        ByteArrayInputStream(expected).copyBoundedTo(output, maxBytes = expected.size.toLong())

        assertArrayEquals(expected, output.toByteArray())
    }

    @Test
    fun boundedCopyRejectsContentOverLimit() {
        val output = ByteArrayOutputStream()

        assertThrows(AppealPhotoTooLargeException::class.java) {
            ByteArrayInputStream(byteArrayOf(1, 2, 3)).copyBoundedTo(output, maxBytes = 2)
        }
        assertTrue(output.size() <= 2)
    }

    @Test
    fun imageSignatureMustMatchSelectedMimeType() {
        val photo = File.createTempFile("appeal-photo-test-", ".png")
        try {
            photo.writeBytes(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A))

            assertTrue(photo.hasImageSignature("image/png"))
            assertFalse(photo.hasImageSignature("image/jpeg"))
        } finally {
            photo.delete()
        }
    }
}
