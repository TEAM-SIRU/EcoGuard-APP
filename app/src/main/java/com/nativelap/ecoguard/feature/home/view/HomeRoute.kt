package com.nativelap.ecoguard.feature.home.view

import androidx.compose.runtime.Composable
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. ViewModel 연결은 홈 기능 작업에서 추가한다.
@Composable
fun HomeRoute(
    uiState: HomeUiState = HomeUiState.Loading,
    onEvent: (HomeScreenEvent) -> Unit = {},
) {
    when (uiState) {
        HomeUiState.Loading -> {
            HomeLoadingScreen(onEvent = onEvent)
        }

        HomeUiState.LoadFailed -> {
            HomeLoadFailedScreen(onEvent = onEvent)
        }

        is HomeUiState.RemovedFromActivity -> {
            HomeActivityRemovedScreen(
                removalReason = uiState.removalReason,
                onEvent = onEvent,
            )
        }

        is HomeUiState.Content -> {
            HomeScreen(
                uiState = uiState,
                onEvent = onEvent,
            )
        }
    }
}
