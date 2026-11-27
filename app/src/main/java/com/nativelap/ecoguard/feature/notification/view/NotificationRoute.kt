package com.nativelap.ecoguard.feature.notification.view

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationEffect
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationScreenEvent
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationViewModel
import com.nativelap.ecoguard.ui.component.EcoBottomTab

@Composable
fun NotificationRoute(
    selectedTab: EcoBottomTab,
    onBack: () -> Unit,
    onTab: (EcoBottomTab) -> Unit,
    onCamera: () -> Unit,
) {
    val viewModel = rememberActivityViewModel<NotificationViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }
    val updateFailedMessage = stringResource(R.string.notification_update_failed)
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NotificationEffect.UpdateFailed -> snackbarState.showSnackbar(updateFailedMessage)
            }
        }
    }
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
                viewModel.markRead(event.notificationId)
            }

            NotificationScreenEvent.RetryClick -> {
                viewModel.retry()
            }

            NotificationScreenEvent.MarkAllReadClick -> {
                viewModel.markAllRead()
            }
        }
    }, snackbarHost = { SnackbarHost(snackbarState) })
}
