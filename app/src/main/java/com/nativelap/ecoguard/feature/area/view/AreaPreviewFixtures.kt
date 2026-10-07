package com.nativelap.ecoguard.feature.area.view

import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState

// Preview 전용 샘플 값. 실제 화면 값은 서버 연동 후 ViewModel이 채운다.
internal object AreaPreviewFixtures {
    val areaContent = AreaUiState.Content(
        floors = listOf(1, 2, 3, 4),
        selectedFloor = 2,
        areaName = "2-1반 앞부터 중앙 계단 앞까지",
        areaDescription = "바닥을 쓸고 창틀 먼지를 닦아요",
        cleaningStartTime = "08:00",
        cleaningEndTime = "08:10",
        teammateNames = listOf("김서연", "이도윤"),
        myName = "최민준",
    )
}
