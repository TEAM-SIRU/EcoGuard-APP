package com.nativelap.ecoguard.feature.appeal.view

import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryItemUiModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealResultUiState
import com.nativelap.ecoguard.ui.component.StatusChipType

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object AppealPreviewFixtures {
    val emptyForm =
        AppealFormUiState(
            rejectionReason = "사진에 청소 구역이 잘 보이지 않아요",
            appealContent = "",
            attachedPhotoCount = 0,
            maxPhotoCount = 3,
            maxContentLength = 300,
            verificationDeadlineTime = "08:10",
        )

    val historyItems =
        listOf(
            AppealHistoryItemUiModel(
                appealId = 3L,
                verificationDate = "9월 22일(화)",
                attemptNumber = 2,
                sentDateTime = "9월 29일(화) 12:20",
                status = StatusChipType.REVIEWING,
                earnedMinutes = null,
            ),
            AppealHistoryItemUiModel(
                appealId = 2L,
                verificationDate = "9월 22일(화)",
                attemptNumber = 1,
                sentDateTime = "9월 22일(화) 13:02",
                status = StatusChipType.REJECTED,
                earnedMinutes = null,
            ),
            AppealHistoryItemUiModel(
                appealId = 1L,
                verificationDate = "9월 21일(월)",
                attemptNumber = 1,
                sentDateTime = "9월 21일(월) 12:40",
                status = StatusChipType.APPROVED,
                earnedMinutes = 10,
            ),
        )

    val approvedResult =
        AppealResultUiState.Approved(
            verificationDate = "9월 21일(월)",
            attemptNumber = 1,
            earnedMinutes = 10,
        )

    val rejectedResult =
        AppealResultUiState.Rejected(
            verificationDate = "9월 22일(화)",
            attemptNumber = 1,
            teacherResponseTitle = "사진에 구역 표지판이 보이지 않아요",
            teacherResponseDetail = "표지판이 보이게 다시 찍어 주세요. 08:10 이후에도 이의신청용 촬영은 가능해요.",
        )
}
