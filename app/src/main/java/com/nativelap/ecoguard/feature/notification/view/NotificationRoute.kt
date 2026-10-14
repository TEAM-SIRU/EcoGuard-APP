package com.nativelap.ecoguard.feature.notification.view

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationScreenEvent
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import kotlinx.coroutines.launch

@Composable
fun NotificationRoute(
    uiState: NotificationUiState,
    selectedTab: EcoBottomTab,
    onBack: () -> Unit,
    onTab: (EcoBottomTab) -> Unit,
    onCamera: () -> Unit,
    onOpenNotification: (Long) -> Boolean,
) {
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val missingMessage = stringResource(R.string.notification_missing)
    NotificationScreen(uiState, selectedTab, onEvent = { event ->
        when (event) {
            NotificationScreenEvent.BackClick -> {
                onBack()
            }

            is NotificationScreenEvent.TabSelect -> {
                onTab(event.tab)
            }

            NotificationScreenEvent.CameraClick -> {
                onCamera()
            }

            is NotificationScreenEvent.NotificationClick -> {
                if (!onOpenNotification(event.notificationId)) {
                    scope.launch { snackbarState.showSnackbar(missingMessage) }
                }
            }

            NotificationScreenEvent.RetryClick -> {
                Unit
            }
        }
    }, snackbarHost = { SnackbarHost(snackbarState) })
}
