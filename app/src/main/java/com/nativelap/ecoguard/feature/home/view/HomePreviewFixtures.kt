package com.nativelap.ecoguard.feature.home.view

import com.nativelap.ecoguard.feature.home.viewmodel.CleaningRecordUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeNoticeUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.WeekCleaningUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.WeekDayUiModel
import com.nativelap.ecoguard.ui.component.StatusChipType
import java.time.DayOfWeek

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object HomePreviewFixtures {
    val notice =
        HomeNoticeUiModel(
            publishedDate = "2026. 09. 01",
            headline = "9월 환경지킴이 활동 안내",
            body = "매일 08:00 – 08:10에 청소하고 사진 1장으로 인증해 주세요.",
            isNew = true,
        )

    val todayStatuses =
        listOf(
            TodayCleaningStatus.NotSubmitted(timeUntilDeadline = "05:32"),
            TodayCleaningStatus.OutsideVerificationTime(availableFromTime = "08:00"),
            TodayCleaningStatus.AiReviewing(submittedTime = "08:04"),
            TodayCleaningStatus.TeacherReviewing(submittedTime = "08:04"),
            TodayCleaningStatus.Approved(earnedMinutes = 10),
            TodayCleaningStatus.Rejected(rejectionReason = "사진에 청소 구역이 잘 보이지 않아요"),
        )

    fun todayCleaning(cleaningStatus: TodayCleaningStatus) =
        TodayCleaningUiModel(
            cleaningStartTime = "08:00",
            cleaningEndTime = "08:10",
            assignedAreaName = "본관 2층 복도 A",
            status = cleaningStatus,
        )

    fun weekCleaning(isTodayCompleted: Boolean): WeekCleaningUiModel {
        val completedDayCount =
            if (isTodayCompleted) {
                2
            } else {
                1
            }

        return WeekCleaningUiModel(
            completedDayCount = completedDayCount,
            totalDayCount = 5,
            days =
                listOf(
                    WeekDayUiModel(
                        dayOfWeek = DayOfWeek.MONDAY,
                        isToday = false,
                        isCompleted = true,
                    ),
                    WeekDayUiModel(
                        dayOfWeek = DayOfWeek.TUESDAY,
                        isToday = true,
                        isCompleted = isTodayCompleted,
                    ),
                    WeekDayUiModel(
                        dayOfWeek = DayOfWeek.WEDNESDAY,
                        isToday = false,
                        isCompleted = false,
                    ),
                    WeekDayUiModel(
                        dayOfWeek = DayOfWeek.THURSDAY,
                        isToday = false,
                        isCompleted = false,
                    ),
                    WeekDayUiModel(
                        dayOfWeek = DayOfWeek.FRIDAY,
                        isToday = false,
                        isCompleted = false,
                    ),
                ),
        )
    }

    val recentRecords =
        listOf(
            CleaningRecordUiModel(
                recordId = 3L,
                submittedDateTime = "9월 28일(월) 08:05",
                areaName = "본관 2층 복도 A",
                status = StatusChipType.APPROVED,
                earnedMinutes = 10,
            ),
            CleaningRecordUiModel(
                recordId = 2L,
                submittedDateTime = "9월 23일(수) 08:09",
                areaName = "본관 2층 복도 A",
                status = StatusChipType.APPROVED,
                earnedMinutes = 10,
            ),
            CleaningRecordUiModel(
                recordId = 1L,
                submittedDateTime = "9월 22일(화) 08:04",
                areaName = "본관 2층 복도 A",
                status = StatusChipType.REJECTED,
                earnedMinutes = 0,
            ),
        )

    val recruiting =
        HomeSectionUiModel.Recruiting(
            semesterName = "2학기",
            maxApplicantsPerClass = 6,
            grade = 2,
            classNumber = 3,
            appliedCount = 4,
        )

    fun cleaningHome(
        cleaningStatus: TodayCleaningStatus,
        hasNotice: Boolean = false,
    ) = HomeUiState.Content(
        notice =
            if (hasNotice) {
                notice
            } else {
                null
            },
        section =
            HomeSectionUiModel.Cleaning(
                todayCleaning = todayCleaning(cleaningStatus),
                weekCleaning = weekCleaning(isTodayCompleted = cleaningStatus is TodayCleaningStatus.Approved),
                recentRecords = recentRecords,
            ),
    )

    val notSubmittedHome =
        cleaningHome(
            cleaningStatus = todayStatuses.first(),
            hasNotice = true,
        )

    val rejectedHome =
        cleaningHome(
            cleaningStatus = todayStatuses.last(),
        )

    val recruitingHome =
        HomeUiState.Content(
            notice = notice,
            section = recruiting,
        )

    val waitingAssignmentHome =
        HomeUiState.Content(
            notice = notice,
            section = HomeSectionUiModel.WaitingAssignment,
        )
}
