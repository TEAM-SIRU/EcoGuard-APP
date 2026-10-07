package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.ui.component.EcoConfirmDialog

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. 로그아웃 확인 다이얼로그 표시 여부만 Route가 관리한다.
@Composable
fun MenuRoute(
    uiState: MenuUiState,
    onEvent: (MenuScreenEvent) -> Unit = {},
    initialLogoutDialogVisible: Boolean = false,
) {
    var isLogoutDialogVisible by rememberSaveable { mutableStateOf(initialLogoutDialogVisible) }

    MenuScreen(
        uiState = uiState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                MenuScreenEvent.LogoutClick -> isLogoutDialogVisible = true
                else -> onEvent(screenEvent)
            }
        },
    )

    if (isLogoutDialogVisible) {
        EcoConfirmDialog(
            title = stringResource(R.string.logout_confirmation_title),
            description = stringResource(R.string.logout_confirmation_description),
            dismissText = stringResource(R.string.action_cancel),
            confirmText = stringResource(R.string.action_logout),
            onDismissRequest = { isLogoutDialogVisible = false },
            onConfirmClick = {
                isLogoutDialogVisible = false
                onEvent(MenuScreenEvent.LogoutConfirm)
            },
            isDestructive = true,
        )
    }
}
