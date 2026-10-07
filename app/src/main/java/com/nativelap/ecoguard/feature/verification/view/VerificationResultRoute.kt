package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.runtime.Composable
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultUiState

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. ViewModel 연결은 인증 결과 기능 작업에서 추가한다.
@Composable
fun VerificationResultRoute(
    uiState: VerificationResultUiState,
    onEvent: (VerificationResultScreenEvent) -> Unit = {},
) {
    VerificationResultScreen(
        uiState = uiState,
        onEvent = onEvent,
    )
}
