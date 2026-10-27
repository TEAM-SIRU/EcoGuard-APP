package com.nativelap.ecoguard.feature.activity.viewmodel

import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityRecord
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityResultStatus
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.ui.component.StatusChipType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale

private val activityRecordDateFormatter = DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREAN)

fun MyActivity.toActivityUiState(today: LocalDate): ActivityUiState.Content? {
    val mappedRecords = records.map { record -> record.toUiModel() ?: return null }
    val currentWeekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekGroups =
        mappedRecords
            .groupBy { (recordDate, _) ->
                recordDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            }.toSortedMap(compareByDescending { it })
            .map { (weekStart, weekRecords) ->
                ActivityWeekGroupUiModel(
                    weeksAgo =
                        ChronoUnit.WEEKS
                            .between(weekStart, currentWeekStart)
                            .toInt()
                            .coerceAtLeast(0),
                    holidayRangeTexts = emptyList(),
                    records = weekRecords.map { (_, uiModel) -> uiModel },
                )
            }

    return ActivityUiState.Content(
        year = year,
        month = month,
        monthlyMinutes = monthlyMinutes,
        approvedCount = summary.approvedCount,
        rejectedCount = summary.rejectedCount,
        notSubmittedCount = summary.notSubmittedCount,
        weekGroups = weekGroups,
    )
}

private data class MappedActivityRecord(
    val date: LocalDate,
    val uiModel: ActivityRecordUiModel,
)

private fun ActivityRecord.toUiModel(): MappedActivityRecord? {
    val parsedDate = runCatching { LocalDate.parse(date) }.getOrNull() ?: return null
    val status = result.toStatusChipType() ?: return null

    return MappedActivityRecord(
        date = parsedDate,
        uiModel =
            ActivityRecordUiModel(
                recordId = verificationId,
                dateLabel = parsedDate.format(activityRecordDateFormatter),
                areaName = area,
                submittedTime = null,
                earnedMinutes = minutes,
                status = status,
            ),
    )
}

private fun ActivityResultStatus.toStatusChipType(): StatusChipType? =
    when (this) {
        ActivityResultStatus.APPROVED -> StatusChipType.APPROVED

        ActivityResultStatus.REJECTED -> StatusChipType.REJECTED

        ActivityResultStatus.PROCESSING,
        ActivityResultStatus.MANUAL_REVIEW,
        -> StatusChipType.REVIEWING

        ActivityResultStatus.NOT_SUBMITTED,
        ActivityResultStatus.UPCOMING,
        -> StatusChipType.NOT_SUBMITTED

        ActivityResultStatus.UNKNOWN -> null
    }
