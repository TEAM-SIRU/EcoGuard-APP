package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuDialogType
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuViewModel
import com.nativelap.ecoguard.ui.component.EcoConfirmDialog
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

// Route가 프로필 상태를 수집하고 화면 이벤트만 상위 내비게이션에 전달한다.
@Composable
fun MenuRoute(
    onEvent: (MenuScreenEvent) -> Unit = {},
    initialDialogType: MenuDialogType? = null,
    useNavigationOverlays: Boolean = false,
    viewModel: MenuViewModel? = null,
) {
    val menuViewModel = viewModel ?: rememberActivityViewModel<MenuViewModel>()
    val uiState by menuViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var visibleDialogType by rememberSaveable {
        mutableStateOf(initialDialogType)
    }
    val contentState = uiState as? MenuUiState.Content

    androidx.compose.runtime.LaunchedEffect(contentState?.isWithdrawn) {
        if (contentState?.isWithdrawn == true) onEvent(MenuScreenEvent.WithdrawalSucceeded)
    }
    androidx.compose.runtime.LaunchedEffect(contentState?.hasWithdrawalFailed) {
        if (contentState?.hasWithdrawalFailed == true) {
            android.widget.Toast
                .makeText(
                    context,
                    R.string.withdrawal_failed,
                    android.widget.Toast.LENGTH_LONG,
                ).show()
        }
    }

    MenuScreen(
        uiState = uiState,
        onRetry = menuViewModel::retry,
        onEvent = { screenEvent ->
            when (screenEvent) {
                MenuScreenEvent.LogoutClick -> {
                    if (useNavigationOverlays) {
                        onEvent(screenEvent)
                    } else {
                        visibleDialogType =
                            MenuDialogType.LOGOUT
                    }
                }

                MenuScreenEvent.WithdrawClick -> {
                    visibleDialogType = MenuDialogType.WITHDRAW
                }

                MenuScreenEvent.WithdrawConfirm -> {
                    menuViewModel.withdraw()
                }

                else -> {
                    onEvent(screenEvent)
                }
            }
        },
    )

    when (visibleDialogType) {
        MenuDialogType.LOGOUT -> {
            EcoConfirmDialog(
                title = stringResource(R.string.logout_confirmation_title),
                description = stringResource(R.string.logout_confirmation_description),
                dismissText = stringResource(R.string.action_cancel),
                confirmText = stringResource(R.string.action_logout),
                onDismissRequest = { visibleDialogType = null },
                onConfirmClick = {
                    visibleDialogType = null
                    onEvent(MenuScreenEvent.LogoutConfirm)
                },
                isDestructive = true,
            )
        }

        MenuDialogType.WITHDRAW -> {
            EcoConfirmDialog(
                title = stringResource(R.string.withdraw_confirmation_title),
                description = stringResource(R.string.withdraw_confirmation_description),
                dismissText = stringResource(R.string.action_cancel),
                confirmText = stringResource(R.string.action_withdraw),
                onDismissRequest = { visibleDialogType = null },
                onConfirmClick = {
                    visibleDialogType = null
                    onEvent(MenuScreenEvent.WithdrawConfirm)
                },
                isDestructive = true,
            )
        }

        null -> {
            Unit
        }
    }
}

@Preview(name = "Menu · logout dialog", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MenuRouteLogoutDialogPreview() {
    EcoGuardTheme {
        MenuScreen(
            uiState = MenuPreviewFixtures.menuState,
            onEvent = {},
        )
    }
}

@Preview(name = "Menu · withdraw dialog", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MenuRouteWithdrawDialogPreview() {
    EcoGuardTheme {
        MenuScreen(
            uiState = MenuPreviewFixtures.menuState,
            onEvent = {},
        )
    }
}
