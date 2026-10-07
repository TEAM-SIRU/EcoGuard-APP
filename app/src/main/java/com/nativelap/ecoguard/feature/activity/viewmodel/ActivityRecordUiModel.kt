package com.nativelap.ecoguard.feature.activity.viewmodel

import androidx.compose.runtime.Immutable
import com.nativelap.ecoguard.ui.component.StatusChipType

@Immutable
data class ActivityRecordUiModel(
    val recordId: Long,
    val dateLabel: String,
    val areaName: String,
    val submittedTime: String?,
    val earnedMinutes: Int,
    val status: StatusChipType,
    val isAppealApproved: Boolean,
)
