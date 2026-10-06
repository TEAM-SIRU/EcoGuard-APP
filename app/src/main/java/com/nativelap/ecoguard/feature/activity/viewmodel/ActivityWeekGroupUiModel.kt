package com.nativelap.ecoguard.feature.activity.viewmodel

import androidx.compose.runtime.Immutable

/** 주 단위 기록 묶음. weeksAgo가 0이면 이번 주, 1이면 지난주다. */
@Immutable
data class ActivityWeekGroupUiModel(
    val weeksAgo: Int,
    val holidayRangeTexts: List<String>,
    val records: List<ActivityRecordUiModel>,
)
