package com.nativelap.ecoguard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class EcoGuardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        cleanExpiredPhotoFiles()
    }

    private fun cleanExpiredPhotoFiles() {
        val expirationTime = System.currentTimeMillis() - PHOTO_CACHE_MAX_AGE_MILLIS
        cacheDir
            .listFiles { file -> file.name.startsWith(APPEAL_PHOTO_PREFIX) }
            ?.filter { it.lastModified() < expirationTime }
            ?.forEach(File::delete)

        File(cacheDir, CAMERA_CAPTURE_DIRECTORY)
            .listFiles()
            ?.filter { it.isFile && it.lastModified() < expirationTime }
            ?.forEach(File::delete)
    }

    private companion object {
        const val APPEAL_PHOTO_PREFIX = "appeal-photo-"
        const val CAMERA_CAPTURE_DIRECTORY = "camera-captures"
        val PHOTO_CACHE_MAX_AGE_MILLIS = TimeUnit.DAYS.toMillis(1)
    }
}
