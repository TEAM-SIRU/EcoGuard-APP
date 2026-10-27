package com.nativelap.ecoguard.feature.verification.viewmodel

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.verification.module.domain.usecase.SubmitVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class VerificationSubmissionViewModel
    @Inject
    constructor(
        private val submitVerification: SubmitVerificationUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<VerificationSubmissionUiState>(VerificationSubmissionUiState.Idle)
        val uiState = mutableUiState.asStateFlow()

        private var requestKey: String? = null
        private var isSubmitting = false

        fun submit(
            photoPath: String,
            areaId: Long,
        ) {
            if (isSubmitting) return
            val photo = File(photoPath)
            if (!photo.isValidForUpload() || areaId <= 0L) {
                mutableUiState.value = VerificationSubmissionUiState.Failed
                return
            }

            val idempotencyKey = requestKey ?: UUID.randomUUID().toString().also { requestKey = it }
            val submitStartedAt = Instant.now().toString()
            isSubmitting = true
            viewModelScope.launch {
                mutableUiState.value = VerificationSubmissionUiState.Submitting
                when (val result = submitVerification(photo, areaId, idempotencyKey, submitStartedAt)) {
                    is AppResult.Success -> {
                        val response = result.value
                        val verificationId = response.verificationId
                        val submittedAt = response.submittedAt
                        if (verificationId == null || submittedAt.isNullOrBlank()) {
                            mutableUiState.value = VerificationSubmissionUiState.Failed
                        } else {
                            photo.delete()
                            requestKey = null
                            mutableUiState.value =
                                VerificationSubmissionUiState.Completed(
                                    verificationId = verificationId,
                                    submittedAt = submittedAt,
                                )
                        }
                    }

                    is AppResult.Failure -> {
                        mutableUiState.value = VerificationSubmissionUiState.Failed
                    }
                }
                isSubmitting = false
            }
        }

        fun reset() {
            if (isSubmitting) return
            requestKey = null
            mutableUiState.value = VerificationSubmissionUiState.Idle
        }
    }

private fun File.isValidForUpload(): Boolean {
    if (!isFile || length() !in 1L..MAX_VERIFICATION_PHOTO_BYTES) return false

    val jpegHeader = ByteArray(3)
    val hasJpegSignature =
        inputStream().use { stream ->
            stream.read(jpegHeader) == jpegHeader.size &&
                jpegHeader[0] == 0xFF.toByte() && jpegHeader[1] == 0xD8.toByte() &&
                jpegHeader[2] == 0xFF.toByte()
        }
    if (!hasJpegSignature) return false

    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(absolutePath, options)
    return options.outWidth > 0 && options.outHeight > 0
}

private const val MAX_VERIFICATION_PHOTO_BYTES = 10L * 1024L * 1024L

sealed interface VerificationSubmissionUiState {
    data object Idle : VerificationSubmissionUiState

    data object Submitting : VerificationSubmissionUiState

    data object Failed : VerificationSubmissionUiState

    data class Completed(
        val verificationId: Long,
        val submittedAt: String,
    ) : VerificationSubmissionUiState
}
