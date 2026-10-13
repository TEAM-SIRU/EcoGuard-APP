package com.nativelap.ecoguard.feature.demo.viewmodel

import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityRecordUiModel
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.feature.home.viewmodel.HomeNoticeUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.WeekCleaningUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.WeekDayUiModel
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiModel
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationTarget
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiModel
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState
import com.nativelap.ecoguard.ui.component.StatusChipType
import java.time.DayOfWeek

// Figma 흐름 탐색용 가상 값이며 운영 데이터·정책이 아니다.
object DemoData {
    const val TODAY_RECORD_ID = 5L
    const val AREA_NAME = "본관 2층 복도 A"
    const val START_TIME = "08:00"
    const val END_TIME = "08:10"
    const val SUBMITTED_TIME = "08:04"
    const val REJECTION_REASON = "사진에 청소 구역이 잘 보이지 않아요"
    val records =
        listOf(
            ActivityRecordUiModel(5, "9월 29일(화)", AREA_NAME, null, 0, StatusChipType.NOT_SUBMITTED, false),
            ActivityRecordUiModel(4, "9월 28일(월)", AREA_NAME, "08:04", 0, StatusChipType.REVIEWING, false),
            ActivityRecordUiModel(3, "9월 23일(수)", AREA_NAME, "08:03", 10, StatusChipType.APPROVED, false),
            ActivityRecordUiModel(2, "9월 22일(화)", AREA_NAME, "08:04", 0, StatusChipType.REJECTED, false),
            ActivityRecordUiModel(1, "9월 21일(월)", AREA_NAME, "08:07", 10, StatusChipType.APPROVED, true),
        )
    val homeNotice =
        HomeNoticeUiModel(
            "2026. 09. 01",
            "9월 환경지킴이 활동 안내",
            "매일 08:00 – 08:10에 청소하고\n사진 1장으로 인증해 주세요.",
            true,
            listOf("08:00 – 08:10", "사진 1장"),
        )
    val todayCleaning = TodayCleaningUiModel(START_TIME, END_TIME, AREA_NAME, TodayCleaningStatus.NotSubmitted("05:32"))
    val weekCleaning =
        WeekCleaningUiModel(
            1,
            5,
            DayOfWeek.entries.take(5).map {
                WeekDayUiModel(it, it == DayOfWeek.TUESDAY, it == DayOfWeek.MONDAY)
            },
        )
    val area =
        AreaUiState.Content(
            listOf(1, 2, 3, 4),
            2,
            "2-1반 앞부터 중앙 계단 앞까지",
            "바닥을 쓸고 창틀 먼지를 닦아요",
            START_TIME,
            END_TIME,
            listOf("김서연", "이도윤"),
            "최민준",
        )
    val recruitment =
        RecruitmentUiState.Content(
            "2학기",
            6,
            "9월 1일(화)",
            "9월 4일(금)",
            START_TIME,
            END_TIME,
            2,
            3,
            4,
            10,
            RecruitmentApplicationStatus.Open(2),
        )
    val notices =
        listOf(
            NoticeUiModel(
                1,
                homeNotice.headline,
                homeNotice.publishedDate,
                "샘플 공지예요.\n\n${homeNotice.body}\n\n담당 구역과 활동 기록은 앱에서 확인할 수 있어요. 실제 일정은 서버 연동 후 안내해요.",
            ),
            NoticeUiModel(
                2,
                "환경지킴이 신청 안내",
                "2026. 08. 31",
                "샘플 공지예요.\n\n모집 공고에서 신청 내용을 확인하고 신청 동기를 작성해 주세요.",
            ),
        )
    val notifications =
        listOf(
            NotificationUiModel(1, "새로운 모집 공고가 있어요", "9월 29일(화) 12:20 보냄", NotificationTarget.Recruitment),
            NotificationUiModel(2, "청소 인증 결과를 확인해 주세요", "9월 29일(화) 12:10 보냄", NotificationTarget.Verification(2)),
            NotificationUiModel(3, "이의신청 결과가 도착했어요", "9월 29일(화) 12:00 보냄", NotificationTarget.Appeal(1)),
            NotificationUiModel(4, "새로운 공지가 있어요", "9월 29일(화) 11:50 보냄", NotificationTarget.Notice(1)),
        )
}
