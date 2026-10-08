package com.nativelap.ecoguard.feature.recruitment.view

import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object RecruitmentPreviewFixtures {
    val openRecruitment =
        RecruitmentUiState.Content(
            semesterName = "2학기",
            maxApplicantsPerClass = 6,
            periodStartDate = "9월 1일(화)",
            periodEndDate = "9월 4일(금)",
            activityStartTime = "08:00",
            activityEndTime = "08:10",
            grade = 2,
            classNumber = 3,
            appliedCount = 4,
            volunteerMinutesPerVerification = 10,
            applicationStatus = RecruitmentApplicationStatus.Open(remainingSpots = 2),
        )

    val closedRecruitment =
        openRecruitment.copy(
            appliedCount = 6,
            applicationStatus = RecruitmentApplicationStatus.Closed,
        )

    val appliedRecruitment =
        openRecruitment.copy(
            applicationStatus =
                RecruitmentApplicationStatus.AlreadyApplied(
                    appliedDateTime = "9월 1일(화) 12:34",
                    applicationOrder = 4,
                ),
        )
}
