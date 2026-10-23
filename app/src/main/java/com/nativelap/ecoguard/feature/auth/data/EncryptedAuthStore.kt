package com.nativelap.ecoguard.feature.auth.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

internal fun clearEncryptedAuthData(
    clearPreferences: () -> Boolean,
    deleteKey: () -> Boolean,
): Boolean {
    if (runCatching(clearPreferences).getOrDefault(false)) return true
    return runCatching(deleteKey).getOrDefault(false)
}

@Singleton
class EncryptedAuthStore
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        @Synchronized
        fun read(): String? = decrypt(preferences.getString(ENCRYPTED_VALUE, null))

        @Synchronized
        fun write(value: String): Boolean =
            runCatching {
                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
                val ciphertext = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
                val packed = cipher.iv + ciphertext
                preferences.edit().putString(ENCRYPTED_VALUE, Base64.encodeToString(packed, Base64.NO_WRAP)).commit()
            }.getOrDefault(false)

        @Synchronized
        fun clear(): Boolean =
            clearEncryptedAuthData(
                clearPreferences = { preferences.edit().remove(ENCRYPTED_VALUE).commit() },
                deleteKey = {
                    KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }.deleteEntry(KEY_ALIAS)
                    true
                },
            )

        private fun decrypt(value: String?): String? {
            if (value == null) return null
            return runCatching {
                val packed = Base64.decode(value, Base64.NO_WRAP)
                val iv = packed.copyOfRange(0, IV_LENGTH)
                val ciphertext = packed.copyOfRange(IV_LENGTH, packed.size)
                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(TAG_LENGTH_BITS, iv))
                cipher.doFinal(ciphertext).toString(Charsets.UTF_8)
            }.getOrElse {
                clear()
                null
            }
        }

        private fun getOrCreateKey(): SecretKey {
            val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
            (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
            keyGenerator.init(
                KeyGenParameterSpec
                    .Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build(),
            )
            return keyGenerator.generateKey()
        }

        private companion object {
            const val PREFERENCES_NAME = "secure_auth_session"
            const val ENCRYPTED_VALUE = "encrypted_value"
            const val KEY_ALIAS = "ecoguard_auth_session_key"
            const val KEYSTORE_PROVIDER = "AndroidKeyStore"
            const val TRANSFORMATION = "AES/GCM/NoPadding"
            const val IV_LENGTH = 12
            const val TAG_LENGTH_BITS = 128
        }
    }
