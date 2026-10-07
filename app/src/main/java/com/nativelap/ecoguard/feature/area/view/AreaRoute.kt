package com.nativelap.ecoguard.feature.area.view

import androidx.compose.runtime.Composable
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. ViewModel 연결은 구역 기능 작업에서 추가한다.
@Composable
fun AreaRoute(
    uiState: AreaUiState = AreaUiState.Loading,
    onEvent: (AreaScreenEvent) -> Unit = {},
) {
    when (uiState) {
        AreaUiState.Loading -> AreaLoadingScreen()
        else -> AreaScreen(
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}
