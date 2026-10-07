package com.nativelap.ecoguard.feature.appeal.viewmodel

import androidx.compose.runtime.Immutable
import com.nativelap.ecoguard.ui.component.StatusChipType

@Immutable
data class AppealHistoryItemUiModel(
    val appealId: Long,
    val verificationDate: String,
    val attemptNumber: Int,
    val sentDateTime: String,
    val status: StatusChipType,
    val earnedMinutes: Int?,
)
