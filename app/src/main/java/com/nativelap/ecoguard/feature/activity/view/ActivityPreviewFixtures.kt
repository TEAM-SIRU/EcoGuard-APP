package com.nativelap.ecoguard.feature.activity.view

import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityRecordUiModel
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityWeekGroupUiModel
import com.nativelap.ecoguard.ui.component.StatusChipType

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object ActivityPreviewFixtures {
    private const val SAMPLE_AREA_NAME = "본관 2층 복도 A"

    private fun approvedRecord(
        recordId: Long,
        dateLabel: String,
        submittedTime: String,
        isAppealApproved: Boolean = false,
    ) = ActivityRecordUiModel(
        recordId = recordId,
        dateLabel = dateLabel,
        areaName = SAMPLE_AREA_NAME,
        submittedTime = submittedTime,
        earnedMinutes = 10,
        status = StatusChipType.APPROVED,
        isAppealApproved = isAppealApproved,
    )

    val weekGroups =
        listOf(
            ActivityWeekGroupUiModel(
                weeksAgo = 0,
                holidayRangeTexts = emptyList(),
                records =
                    listOf(
                        ActivityRecordUiModel(
                            recordId = 10L,
                            dateLabel = "9월 29일(화)",
                            areaName = SAMPLE_AREA_NAME,
                            submittedTime = "08:04",
                            earnedMinutes = 0,
                            status = StatusChipType.REVIEWING,
                            isAppealApproved = false,
                        ),
                        approvedRecord(recordId = 9L, dateLabel = "9월 28일(월)", submittedTime = "08:05"),
                    ),
            ),
            ActivityWeekGroupUiModel(
                weeksAgo = 1,
                holidayRangeTexts = listOf("9월 24–25일"),
                records =
                    listOf(
                        approvedRecord(recordId = 8L, dateLabel = "9월 23일(수)", submittedTime = "08:09"),
                        ActivityRecordUiModel(
                            recordId = 7L,
                            dateLabel = "9월 22일(화)",
                            areaName = SAMPLE_AREA_NAME,
                            submittedTime = "08:04",
                            earnedMinutes = 0,
                            status = StatusChipType.REJECTED,
                            isAppealApproved = false,
                        ),
                        approvedRecord(
                            recordId = 6L,
                            dateLabel = "9월 21일(월)",
                            submittedTime = "08:07",
                            isAppealApproved = true,
                        ),
                    ),
            ),
            ActivityWeekGroupUiModel(
                weeksAgo = 2,
                holidayRangeTexts = emptyList(),
                records =
                    listOf(
                        approvedRecord(recordId = 5L, dateLabel = "9월 18일(금)", submittedTime = "08:02"),
                        ActivityRecordUiModel(
                            recordId = 4L,
                            dateLabel = "9월 17일(목)",
                            areaName = SAMPLE_AREA_NAME,
                            submittedTime = null,
                            earnedMinutes = 0,
                            status = StatusChipType.NOT_SUBMITTED,
                            isAppealApproved = false,
                        ),
                        approvedRecord(recordId = 3L, dateLabel = "9월 16일(수)", submittedTime = "08:06"),
                        approvedRecord(recordId = 2L, dateLabel = "9월 15일(화)", submittedTime = "08:01"),
                        approvedRecord(recordId = 1L, dateLabel = "9월 14일(월)", submittedTime = "08:03"),
                    ),
            ),
        )

    val activityContent =
        ActivityUiState.Content(
            year = 2026,
            month = 9,
            monthlyMinutes = 70,
            approvedCount = 7,
            rejectedCount = 1,
            notSubmittedCount = 1,
            weekGroups = weekGroups,
        )

    val emptyContent =
        activityContent.copy(
            monthlyMinutes = 0,
            approvedCount = 0,
            rejectedCount = 0,
            notSubmittedCount = 0,
            weekGroups = emptyList(),
        )
}
