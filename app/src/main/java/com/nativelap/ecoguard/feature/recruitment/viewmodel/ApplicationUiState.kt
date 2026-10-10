package com.nativelap.ecoguard.feature.recruitment.viewmodel

import androidx.compose.runtime.Immutable
import com.nativelap.ecoguard.ui.component.TextInputLength

@Immutable
data class ApplicationUiState(
    val grade: Int,
    val classNumber: Int,
    val studentNumber: Int,
    val studentName: String,
    val motivation: String = "",
    val maxApplicantsPerClass: Int = 6,
    val isApplying: Boolean = false,
) {
    val canApply: Boolean
        get() = motivation.isNotBlank() && TextInputLength.count(motivation) <= MAX_MOTIVATION_LENGTH && !isApplying

    companion object {
        const val MAX_MOTIVATION_LENGTH = 200
    }
}
