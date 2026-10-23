package com.nativelap.ecoguard.feature.auth.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EncryptedAuthStoreTest {
    @Test
    fun `clear uses preferences removal when it succeeds`() {
        var keyDeleted = false

        val cleared =
            clearEncryptedAuthData(clearPreferences = { true }, deleteKey = {
                keyDeleted = true
                true
            })

        assertTrue(cleared)
        assertFalse(keyDeleted)
    }

    @Test
    fun `clear deletes encryption key when preferences removal fails`() {
        var keyDeleted = false

        val cleared =
            clearEncryptedAuthData(clearPreferences = { false }, deleteKey = {
                keyDeleted = true
                true
            })

        assertTrue(cleared)
        assertTrue(keyDeleted)
    }

    @Test
    fun `clear reports failure when preferences and encryption key removal fail`() {
        val cleared = clearEncryptedAuthData(clearPreferences = { false }, deleteKey = { false })

        assertFalse(cleared)
    }

    @Test
    fun `clear attempts key deletion when preferences removal throws`() {
        var keyDeleted = false

        val cleared =
            clearEncryptedAuthData(clearPreferences = { error("storage failure") }, deleteKey = {
                keyDeleted =
                    true
                ; true
            })

        assertTrue(cleared)
        assertTrue(keyDeleted)
    }
}
