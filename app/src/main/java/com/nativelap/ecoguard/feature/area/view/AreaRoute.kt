package com.nativelap.ecoguard.feature.area.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.ui.component.LoadingSkeletonLayout

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. ViewModel 연결은 구역 기능 작업에서 추가한다.
@Composable
fun AreaRoute(
    uiState: AreaUiState = AreaUiState.Loading,
    onEvent: (AreaScreenEvent) -> Unit = {},
) {
    when (uiState) {
        AreaUiState.Loading -> LoadingSkeletonLayout(
            title = stringResource(R.string.common_my_cleaning_area),
        )
        else -> AreaScreen(
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}
