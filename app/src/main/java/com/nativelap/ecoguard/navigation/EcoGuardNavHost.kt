package com.nativelap.ecoguard.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.nativelap.ecoguard.feature.auth.presentation.AuthUiState
import com.nativelap.ecoguard.feature.demo.view.DemoDestinationRoute
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoAppState
import com.nativelap.ecoguard.feature.demo.viewmodel.rememberDemoAppState
import com.nativelap.ecoguard.feature.login.view.LoginRoute
import com.nativelap.ecoguard.feature.login.view.TeacherAccountGuideRoute
import com.nativelap.ecoguard.feature.login.viewmodel.LoginUiState

@Composable
fun EcoGuardNavHost(
    authUiState: AuthUiState,
    onLoginClick: () -> Unit,
    onSignOut: () -> Unit,
) {
    val sessionKey = authUiState.sessionId ?: SIGNED_OUT_KEY
    key(sessionKey) {
        if (authUiState.sessionId != null) {
            EcoGuardMainNavDisplay(onSignOut = onSignOut)
        } else {
            EcoGuardAuthNavDisplay(
                uiState =
                    LoginUiState(
                        isLoggingIn = authUiState.isLoggingIn,
                        hasLoginFailed = authUiState.hasLoginFailed,
                        hasSessionClearFailed = authUiState.hasSessionClearFailed,
                        hasServerLogoutFailed = authUiState.hasServerLogoutFailed,
                        hasRetryableLogoutPending = authUiState.hasRetryableLogoutPending,
                    ),
                onLoginClick = onLoginClick,
            )
        }
    }
}

private const val SIGNED_OUT_KEY = "signed-out"

@Composable
private fun EcoGuardAuthNavDisplay(
    uiState: LoginUiState,
    onLoginClick: () -> Unit,
) {
    val backStack = rememberNavBackStack(EcoGuardDestination.Login)
    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider =
            androidx.navigation3.runtime.entryProvider {
                entry<EcoGuardDestination.Login> {
                    LoginRoute(
                        uiState = uiState,
                        onLoginClick = onLoginClick,
                        onTeacherAccountGuideClick = {
                            backStack.add(EcoGuardDestination.TeacherAccountGuide)
                        },
                    )
                }
                entry<EcoGuardDestination.TeacherAccountGuide> {
                    TeacherAccountGuideRoute(
                        onLogoutClick = {
                            if (backStack.size > 1) backStack.removeLastOrNull()
                        },
                    )
                }
            },
    )
}

@Composable
internal fun EcoGuardMainNavDisplay(
    onSignOut: () -> Unit,
    navigationState: EcoGuardMainNavigationState = rememberEcoGuardMainNavigationState(),
    demoState: DemoAppState = rememberDemoAppState(),
) {
    val navigator = remember(navigationState) { EcoGuardMainNavigator(navigationState) }
    val entryProvider: (NavKey) -> NavEntry<NavKey> = { navKey ->
        val destination = navKey as EcoGuardDestination
        val overlayMetadata =
            when (destination) {
                EcoGuardDestination.MonthPicker,
                EcoGuardDestination.LogoutConfirm,
                EcoGuardDestination.WithdrawConfirm,
                -> {
                    DialogSceneStrategy.dialog(
                        DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
                    )
                }

                is EcoGuardDestination.VerificationBlocked -> {
                    EcoGuardBottomSheetSceneStrategy.bottomSheet()
                }

                else -> {
                    emptyMap()
                }
            }
        NavEntry(navKey, metadata = overlayMetadata) {
            DemoDestinationRoute(destination, demoState, navigator, navigationState.selectedTab, onSignOut)
        }
    }
    val entries = navigationState.toEntries(entryProvider)
    // 다른 탭의 첫 화면에서는 홈으로 돌아가고 홈 첫 화면에서는 Activity에 종료를 맡긴다.
    BackHandler(
        enabled = navigationState.currentBackStack.size == 1 && navigationState.canGoBack,
    ) { navigator.goBack() }
    NavDisplay(
        entries = entries,
        onBack = { navigator.goBack() },
        sceneStrategies = listOf(DialogSceneStrategy(), EcoGuardBottomSheetSceneStrategy()),
    )
}
