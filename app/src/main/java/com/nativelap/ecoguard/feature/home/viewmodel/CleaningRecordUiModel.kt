package com.nativelap.ecoguard.feature.home.viewmodel

import androidx.compose.runtime.Immutable
import com.nativelap.ecoguard.ui.component.StatusChipType

@Immutable
data class CleaningRecordUiModel(
    val recordId: Long,
    val submittedDateTime: String,
    val areaName: String,
    val status: StatusChipType,
    val earnedMinutes: Int,
)
