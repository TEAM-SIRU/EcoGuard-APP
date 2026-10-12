package com.nativelap.ecoguard.feature.menu.view

import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object MenuPreviewFixtures {
    val menuState =
        MenuUiState(
            studentName = "최민준",
            grade = 2,
            classNumber = 3,
            monthlyApprovalCount = 7,
            monthlyMinutes = 70,
            assignedAreaName = "본관 2층 복도 A",
            isApplicationCompleted = true,
            isCleaningNotificationEnabled = true,
        )
}
