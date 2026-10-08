package com.nativelap.ecoguard.feature.login.view

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
import androidx.compose.ui.layout.Layout
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            CenteredGroupWithFooter(
                modifier =
                    Modifier
                        .widthIn(max = AppComponentSize.contentMaxWidth)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .heightIn(min = maxHeight)
                        .padding(
                            horizontal = AppSpacing.screenHorizontal,
                            vertical = AppSpacing.xl,
                        ),
                footerSpacing = loginToastTopSpacing,
                groupContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LoginBrand()

                        LoginActionContent(
                            isLoggingIn = uiState.isLoggingIn,
                            hasLoginFailed = uiState.hasLoginFailed,
                            onLoginClick = { onEvent(LoginScreenEvent.LoginClick) },
                            modifier = Modifier.padding(top = AppSpacing.sm),
                        )
                    }
                },
                footerContent = {
                    if (uiState.hasLoginFailed) {
                        EcoToast(message = stringResource(R.string.login_failed))
                    }
                },
            )
        }
    }
}

/**
 * 로고·버튼 묶음을 세로 가운데에 두고, 그 아래에 토스트(footer)를 붙인다.
 * Figma처럼 토스트가 있어도 묶음은 가운데를 유지하고, 큰 글자 등으로 공간이 부족하면
 * 토스트가 가려지지 않도록 묶음을 위로 올린다. 그래도 넘치면 스크롤로 볼 수 있다.
 */
@Composable
private fun CenteredGroupWithFooter(
    footerSpacing: Dp,
    groupContent: @Composable () -> Unit,
    footerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Layout(
        contents = listOf(groupContent, footerContent),
        modifier = modifier,
    ) { (groupMeasurables, footerMeasurables), constraints ->
        val childConstraints =
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
            )
        val groupPlaceables =
            groupMeasurables.map { groupMeasurable ->
                groupMeasurable.measure(childConstraints)
            }
        val footerPlaceables =
            footerMeasurables.map { footerMeasurable ->
                footerMeasurable.measure(childConstraints)
            }
        val groupHeight = groupPlaceables.sumOf { groupPlaceable -> groupPlaceable.height }
        val footerHeight = footerPlaceables.sumOf { footerPlaceable -> footerPlaceable.height }
        val footerBlockHeight =
            if (footerPlaceables.isEmpty() || footerHeight == 0) {
                0
            } else {
                footerSpacing.roundToPx() + footerHeight
            }
        val layoutHeight =
            maxOf(
                constraints.minHeight,
                groupHeight + footerBlockHeight,
            )
        // 공간이 충분하면 묶음을 세로 가운데에 두고, 부족하면 토스트가 화면 안에 보이도록 위로 올린다.
        val groupTop =
            ((layoutHeight - groupHeight) / 2).coerceIn(
                minimumValue = 0,
                maximumValue = layoutHeight - groupHeight - footerBlockHeight,
            )

        layout(constraints.maxWidth, layoutHeight) {
            var placeY = groupTop

            groupPlaceables.forEach { groupPlaceable ->
                groupPlaceable.place(
                    x = (constraints.maxWidth - groupPlaceable.width) / 2,
                    y = placeY,
                )
                placeY += groupPlaceable.height
            }

            placeY += footerSpacing.roundToPx()

            footerPlaceables.forEach { footerPlaceable ->
                footerPlaceable.place(
                    x = (constraints.maxWidth - footerPlaceable.width) / 2,
                    y = placeY,
                )
                placeY += footerPlaceable.height
            }
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
