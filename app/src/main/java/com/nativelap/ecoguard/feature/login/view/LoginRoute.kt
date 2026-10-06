package com.nativelap.ecoguard.feature.login.view

import androidx.compose.runtime.Composable
import com.nativelap.ecoguard.feature.login.viewmodel.LoginScreenEvent
import com.nativelap.ecoguard.feature.login.viewmodel.LoginUiState

// 서버 연동 전까지는 고정 상태를 표시한다. ViewModel 연결은 로그인 기능 작업에서 추가한다.
@Composable
fun LoginRoute(
    onLoginClick: () -> Unit = {},
) {
    LoginScreen(
        uiState = LoginUiState(),
        onEvent = { screenEvent ->
            when (screenEvent) {
                LoginScreenEvent.LoginClick -> onLoginClick()
            }
        },
    )
}
