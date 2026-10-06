package com.nativelap.ecoguard.feature.verification.viewmodel

sealed interface VerificationResultScreenEvent {
    data object ConfirmClick : VerificationResultScreenEvent

    data object AppealClick : VerificationResultScreenEvent

    data object RetryClick : VerificationResultScreenEvent

    data object HomeClick : VerificationResultScreenEvent

    data object BackClick : VerificationResultScreenEvent
}
