package com.nativelap.ecoguard.feature.verification.viewmodel

sealed interface VerificationScreenEvent {
    data object BackClick : VerificationScreenEvent

    data object CloseClick : VerificationScreenEvent

    data object TakePhotoClick : VerificationScreenEvent

    data object ShutterClick : VerificationScreenEvent

    data object FlipCameraClick : VerificationScreenEvent

    data object RetakeClick : VerificationScreenEvent

    data object SendClick : VerificationScreenEvent

    data object ResendSamePhotoClick : VerificationScreenEvent

    data object CheckUploadStatusClick : VerificationScreenEvent

    data object HomeClick : VerificationScreenEvent
}
