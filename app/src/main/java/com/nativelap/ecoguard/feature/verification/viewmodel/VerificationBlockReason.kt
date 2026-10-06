package com.nativelap.ecoguard.feature.verification.viewmodel

import androidx.compose.runtime.Immutable

/** 청소 인증을 시작할 수 없을 때 바텀시트로 안내하는 이유. */
@Immutable
sealed interface VerificationBlockReason {
    data class OutsideVerificationTime(
        val startTime: String,
        val endTime: String,
    ) : VerificationBlockReason

    data class AlreadySubmitted(
        val submittedTime: String,
    ) : VerificationBlockReason

    data object CameraPermissionRequired : VerificationBlockReason
}
