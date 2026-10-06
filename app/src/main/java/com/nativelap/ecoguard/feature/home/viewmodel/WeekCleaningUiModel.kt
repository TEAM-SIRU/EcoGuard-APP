package com.nativelap.ecoguard.feature.home.viewmodel

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek

@Immutable
data class WeekCleaningUiModel(
    val completedDayCount: Int,
    val totalDayCount: Int,
    val days: List<WeekDayUiModel>,
)

@Immutable
data class WeekDayUiModel(
    val dayOfWeek: DayOfWeek,
    val isToday: Boolean,
    val isCompleted: Boolean,
)
