package com.nativelap.ecoguard.feature.verification.viewmodel

/** 청소 인증 3단계(촬영 안내 → 촬영 → 확인)의 순서. */
enum class VerificationStep(
    val stepNumber: Int,
) {
    GUIDE(stepNumber = 1),
    CAPTURE(stepNumber = 2),
    CONFIRM(stepNumber = 3),
    ;

    companion object {
        val totalStepCount = entries.size
    }
}
