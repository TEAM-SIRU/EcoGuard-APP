package com.nativelap.ecoguard.feature.menu.view

import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object MenuPreviewFixtures {
    val menuState =
        MenuUiState.Content(
            userName = "최민준",
            studentNumber = "05",
            grade = 2,
            classNumber = 3,
        )
}
