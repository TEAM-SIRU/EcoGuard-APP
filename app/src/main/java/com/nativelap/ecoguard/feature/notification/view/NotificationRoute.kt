package com.nativelap.ecoguard.feature.notification.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationScreenEvent
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiModel
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationViewModel
import com.nativelap.ecoguard.ui.component.EcoBottomTab

@Composable
fun NotificationRoute(
    selectedTab: EcoBottomTab,
    onBack: () -> Unit,
    onTab: (EcoBottomTab) -> Unit,
    onCamera: () -> Unit,
    viewModel: NotificationViewModel = rememberActivityViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) { viewModel.load() }
    var selectedNotification by remember { mutableStateOf<NotificationUiModel?>(null) }
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
                val content = uiState as? NotificationUiState.Content
                val notification = content?.notifications?.find { it.notificationId == event.notificationId }
                if (content != null && !content.isUpdating && notification != null) {
                    selectedNotification = notification
                    if (!notification.isRead) {
                        viewModel.markRead(notification.notificationId)
                    }
                }
            }

            NotificationScreenEvent.RetryClick -> {
                viewModel.load()
            }

            NotificationScreenEvent.ReadAllClick -> {
                viewModel.markAllRead()
            }
        }
    })
    selectedNotification?.let { notification ->
        AlertDialog(
            onDismissRequest = { selectedNotification = null },
            title = { Text(notification.title) },
            text = { Text(notification.body) },
            confirmButton = {
                TextButton(onClick = { selectedNotification = null }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
        )
    }
}
