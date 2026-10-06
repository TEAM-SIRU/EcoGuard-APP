package com.nativelap.ecoguard.feature.login.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.login.viewmodel.LoginScreenEvent
import com.nativelap.ecoguard.feature.login.viewmodel.LoginUiState
import com.nativelap.ecoguard.ui.component.EcoToast
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

// Figma 하단 CTA 영역 아래 여백(34) + 토스트 상단 여백(8)
private val loginToastTopSpacing = 42.dp

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEvent: (LoginScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .padding(
                        horizontal = AppSpacing.screenHorizontal,
                        vertical = AppSpacing.xl,
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoginBrand()

                LoginActionContent(
                    isLoggingIn = uiState.isLoggingIn,
                    hasLoginFailed = uiState.hasLoginFailed,
                    onLoginClick = { onEvent(LoginScreenEvent.LoginClick) },
                    modifier = Modifier.padding(top = AppSpacing.sm),
                )

                if (uiState.hasLoginFailed) {
                    EcoToast(
                        message = stringResource(R.string.login_failed),
                        modifier = Modifier.withoutLayoutHeight(topOffset = loginToastTopSpacing),
                    )
                }
            }
        }
    }
}

// Figma처럼 토스트가 로고·버튼 묶음의 세로 중앙 정렬에 영향을 주지 않도록 높이를 0으로 측정한다.
private fun Modifier.withoutLayoutHeight(topOffset: Dp): Modifier {
    return layout { measurable, constraints ->
        val toastPlaceable = measurable.measure(constraints)

        layout(toastPlaceable.width, 0) {
            toastPlaceable.place(
                x = 0,
                y = topOffset.roundToPx(),
            )
        }
    }
}

@Preview(name = "Login · default", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Login · compact", showBackground = true, widthDp = 320, heightDp = 596)
@Preview(name = "Login · larger text", showBackground = true, widthDp = 390, heightDp = 844, fontScale = 1.5f)
@Composable
private fun LoginScreenPreview() {
    EcoGuardTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onEvent = {},
        )
    }
}

@Preview(name = "Login · loading", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LoginScreenLoadingPreview() {
    EcoGuardTheme {
        LoginScreen(
            uiState = LoginUiState(isLoggingIn = true),
            onEvent = {},
        )
    }
}

@Preview(name = "Login · failed", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Login · failed tablet", showBackground = true, widthDp = 840, heightDp = 900)
@Composable
private fun LoginScreenFailedPreview() {
    EcoGuardTheme {
        LoginScreen(
            uiState = LoginUiState(hasLoginFailed = true),
            onEvent = {},
        )
    }
}
