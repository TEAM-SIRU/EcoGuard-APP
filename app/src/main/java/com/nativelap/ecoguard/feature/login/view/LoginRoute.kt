package com.nativelap.ecoguard.feature.login.view

import androidx.compose.runtime.Composable
import com.nativelap.ecoguard.feature.login.viewmodel.LoginScreenEvent
import com.nativelap.ecoguard.feature.login.viewmodel.LoginUiState

// 인증 상태와 화면 이벤트를 연결하고, 로그인 화면은 전달된 상태만 표시한다.
@Composable
fun LoginRoute(
    uiState: LoginUiState = LoginUiState(),
    onLoginClick: () -> Unit = {},
    onTeacherAccountGuideClick: () -> Unit = {},
) {
    LoginScreen(
        uiState = uiState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                LoginScreenEvent.LoginClick -> onLoginClick()
                LoginScreenEvent.TeacherAccountGuideClick -> onTeacherAccountGuideClick()
            }
        },
    )
}
