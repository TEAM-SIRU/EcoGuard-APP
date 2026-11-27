package com.nativelap.ecoguard.feature.notification.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationSettingUiModel
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationSettingsUiState
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SkeletonPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun NotificationSettingsScreen(
    uiState: NotificationSettingsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onSettingChanged: (String, Boolean) -> Unit,
    snackbarHost: @Composable () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { EcoBackTopBar(onBackClick = onBack) },
        snackbarHost = snackbarHost,
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(Modifier.widthIn(max = AppComponentSize.contentMaxWidth).fillMaxSize()) {
                PageTitle(
                    stringResource(R.string.notification_settings_title),
                    Modifier.padding(
                        start = AppSpacing.xl,
                        end = AppSpacing.xl,
                        top = AppSpacing.xs,
                        bottom = AppSpacing.sm,
                    ),
                )
                Text(
                    stringResource(R.string.notification_settings_description),
                    Modifier.padding(horizontal = AppSpacing.xl),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.extraColors.captionTextColor,
                )
                LazyColumn(
                    Modifier.fillMaxWidth().weight(1f).padding(top = AppSpacing.lg),
                    contentPadding =
                        androidx.compose.foundation.layout
                            .PaddingValues(AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                ) {
                    when (uiState) {
                        is NotificationSettingsUiState.Content -> {
                            items(uiState.settings, key = { setting -> setting.category }) { setting ->
                                NotificationSettingRow(
                                    setting = setting,
                                    enabled = uiState.updatingCategory == null,
                                    onCheckedChange = { isEnabled ->
                                        onSettingChanged(setting.category, isEnabled)
                                    },
                                )
                            }
                        }

                        NotificationSettingsUiState.Loading -> {
                            items(3) {
                                SkeletonPlaceholder(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(AppComponentSize.logo)
                                        .padding(vertical = AppSpacing.sm),
                                )
                            }
                        }

                        NotificationSettingsUiState.Empty -> {
                            item {
                                InlineEmptyState(
                                    R.drawable.ic_map_30,
                                    stringResource(R.string.notification_settings_empty),
                                    stringResource(R.string.common_try_again_later),
                                    Modifier.padding(vertical = AppSpacing.xl),
                                )
                            }
                        }

                        NotificationSettingsUiState.Failed -> {
                            item {
                                InlineEmptyState(
                                    R.drawable.ic_map_30,
                                    stringResource(R.string.notification_failed),
                                    stringResource(R.string.common_try_again_later),
                                    actionText = stringResource(R.string.action_retry),
                                    onActionClick = onRetry,
                                    modifier = Modifier.padding(vertical = AppSpacing.xl),
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
private fun NotificationSettingRow(
    setting: NotificationSettingUiModel,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    androidx.compose.material3.Card(
        shape =
            androidx.compose.foundation.shape
                .RoundedCornerShape(AppRadius.listCard),
        colors =
            androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                setting.label,
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Switch(
                checked = setting.isEnabled,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
            )
        }
    }
}
