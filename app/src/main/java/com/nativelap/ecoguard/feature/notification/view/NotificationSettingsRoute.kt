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
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationSettingsEffect
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationSettingsViewModel

@Composable
fun NotificationSettingsRoute(onBack: () -> Unit) {
    val viewModel = rememberActivityViewModel<NotificationSettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }
    val updateFailedMessage = stringResource(R.string.notification_update_failed)

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NotificationSettingsEffect.UpdateFailed -> snackbarState.showSnackbar(updateFailedMessage)
            }
        }
    }

    NotificationSettingsScreen(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::retry,
        onSettingChanged = viewModel::updateSetting,
        snackbarHost = { SnackbarHost(snackbarState) },
    )
}
