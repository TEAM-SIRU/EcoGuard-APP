package com.nativelap.ecoguard.feature.notification.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationScreenEvent
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SkeletonPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    selectedTab: EcoBottomTab,
    onEvent: (NotificationScreenEvent) -> Unit,
    snackbarHost: @Composable () -> Unit = {},
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = snackbarHost,
        topBar = { EcoBackTopBar(onBackClick = { onEvent(NotificationScreenEvent.BackClick) }) },
        bottomBar = {
            EcoBottomTabBar(
                selectedTab,
                { onEvent(NotificationScreenEvent.TabSelect(it)) },
                { onEvent(NotificationScreenEvent.CameraClick) },
            )
        },
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(Modifier.widthIn(max = AppComponentSize.contentMaxWidth).fillMaxSize()) {
                PageTitle(
                    stringResource(R.string.notification_title),
                    Modifier.padding(
                        start = AppSpacing.xl,
                        end = AppSpacing.xl,
                        top = AppSpacing.xs,
                        bottom = AppSpacing.lg,
                    ),
                )
                LazyColumn(
                    Modifier.fillMaxWidth().weight(1f),
                    contentPadding =
                        PaddingValues(
                            start = AppSpacing.md,
                            end = AppSpacing.md,
                            top = AppSpacing.md,
                            bottom = AppComponentSize.cameraFabScrollClearance,
                        ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                ) {
                    when (uiState) {
                        is NotificationUiState.Content -> {
                            item { NotificationCount(uiState.notifications.size) }
                            items(uiState.notifications, key = { it.notificationId }) { notification ->
                                NotificationCard(
                                    notification,
                                    { onEvent(NotificationScreenEvent.NotificationClick(notification.notificationId)) },
                                )
                            }
                        }

                        NotificationUiState.Loading -> {
                            items(3) { SkeletonPlaceholder(Modifier.fillMaxWidth().height(AppComponentSize.logo)) }
                        }

                        NotificationUiState.Empty -> {
                            item { NotificationCount(0) }
                            item {
                                InlineEmptyState(
                                    R.drawable.ic_map_30,
                                    stringResource(R.string.notification_empty),
                                    stringResource(R.string.notification_empty_description),
                                    Modifier.padding(vertical = AppSpacing.xl),
                                )
                            }
                        }

                        NotificationUiState.Failed -> {
                            item {
                                InlineEmptyState(
                                    R.drawable.ic_map_30,
                                    stringResource(R.string.notification_failed),
                                    stringResource(R.string.common_try_again_later),
                                    actionText = stringResource(R.string.action_retry),
                                    onActionClick = { onEvent(NotificationScreenEvent.RetryClick) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCount(count: Int) {
    Text(
        stringResource(R.string.format_total_count, count),
        Modifier.padding(horizontal = AppSpacing.xs),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.extraColors.captionTextColor,
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NotificationPreview() {
    EcoGuardTheme {
        NotificationScreen(
            NotificationUiState.Content(
                com.nativelap.ecoguard.feature.demo.viewmodel.DemoData.notifications,
            ),
            EcoBottomTab.HOME,
            {
            },
        )
    }
}
