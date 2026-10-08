package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuDialogType
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.ui.component.EcoConfirmDialog
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. 로그아웃·회원탈퇴 확인 다이얼로그 표시 여부만 Route가 관리한다.
@Composable
fun MenuRoute(
    uiState: MenuUiState,
    onEvent: (MenuScreenEvent) -> Unit = {},
    initialDialogType: MenuDialogType? = null,
) {
    var visibleDialogType by rememberSaveable { mutableStateOf(initialDialogType) }

    MenuScreen(
        uiState = uiState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                MenuScreenEvent.LogoutClick -> visibleDialogType = MenuDialogType.LOGOUT
                MenuScreenEvent.WithdrawClick -> visibleDialogType = MenuDialogType.WITHDRAW
                else -> onEvent(screenEvent)
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
        MenuRoute(
            uiState = MenuPreviewFixtures.menuState,
            initialDialogType = MenuDialogType.LOGOUT,
        )
    }
}

@Preview(name = "Menu · withdraw dialog", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MenuRouteWithdrawDialogPreview() {
    EcoGuardTheme {
        MenuRoute(
            uiState = MenuPreviewFixtures.menuState,
            initialDialogType = MenuDialogType.WITHDRAW,
        )
    }
}
