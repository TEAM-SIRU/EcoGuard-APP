package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent

// 서버 연동 전까지 입력 내용만 Route에서 보관한다. ViewModel 연결은 이의신청 기능 작업에서 추가한다.
@Composable
fun AppealFormRoute(
    initialUiState: AppealFormUiState,
    onEvent: (AppealScreenEvent) -> Unit = {},
) {
    var appealContent by rememberSaveable { mutableStateOf(initialUiState.appealContent) }

    AppealFormScreen(
        uiState = initialUiState.copy(appealContent = appealContent),
        onEvent = { screenEvent ->
            when (screenEvent) {
                is AppealScreenEvent.ContentChange -> appealContent = screenEvent.appealContent
                else -> onEvent(screenEvent)
            }
        },
    )
}
