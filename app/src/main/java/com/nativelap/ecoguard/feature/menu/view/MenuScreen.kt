package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.ui.component.EcoAdaptiveNavigationLayout
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.MenuRow
import com.nativelap.ecoguard.ui.component.SectionDivider
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 서버 프로필과 실제 기능으로 이동하는 메뉴를 표시한다. */
@Composable
fun MenuScreen(
    uiState: MenuUiState,
    onEvent: (MenuScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    EcoAdaptiveNavigationLayout(
        selectedTab = EcoBottomTab.MY_PAGE,
        onTabSelected = { selectedTab -> onEvent(MenuScreenEvent.TabSelect(selectedTab)) },
        onCameraClick = { onEvent(MenuScreenEvent.CameraClick) },
        modifier = modifier,
    ) { usesNavigationRail ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (!usesNavigationRail) {
                    EcoBottomTabBar(
                        selectedTab = EcoBottomTab.MY_PAGE,
                        onTabSelected = { selectedTab -> onEvent(MenuScreenEvent.TabSelect(selectedTab)) },
                        onCameraClick = { onEvent(MenuScreenEvent.CameraClick) },
                    )
                }
            },
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier =
                        Modifier
                            .widthIn(max = AppComponentSize.contentMaxWidth)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = AppComponentSize.cameraFabScrollClearance),
                ) {
                    when (uiState) {
                        MenuUiState.Loading -> {
                            Box(
                                Modifier.fillMaxWidth().padding(vertical = AppSpacing.xl),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        MenuUiState.Failed -> {
                            InlineEmptyState(
                                iconRes = R.drawable.ic_map_30,
                                title = stringResource(R.string.result_load_failed),
                                description = stringResource(R.string.common_try_again_later),
                                actionText = stringResource(R.string.action_retry),
                                onActionClick = onRetry,
                                modifier = Modifier.padding(AppSpacing.xl),
                            )
                        }

                        is MenuUiState.Content -> {
                            MenuProfileHeader(
                                menuState = uiState,
                                modifier =
                                    Modifier.padding(
                                        start = AppSpacing.screenHorizontal,
                                        end = AppSpacing.screenHorizontal,
                                        top = AppSpacing.md,
                                        bottom = AppSpacing.lg,
                                    ),
                            )
                            SectionDivider()
                            EcoCard(
                                modifier = Modifier.padding(horizontal = AppSpacing.md),
                                cornerRadius = AppRadius.listCard,
                                contentPadding = PaddingValues(vertical = AppSpacing.xxs),
                            ) {
                                MenuRow(
                                    title = stringResource(R.string.common_my_cleaning_area),
                                    onClick = { onEvent(MenuScreenEvent.AreaClick) },
                                )
                                MenuRow(
                                    title = stringResource(R.string.profile_application_result),
                                    onClick = { onEvent(MenuScreenEvent.ApplicationResultClick) },
                                )
                                MenuRow(
                                    title = stringResource(R.string.profile_cleaning_notification),
                                    valueText = stringResource(R.string.help_later),
                                    enabled = false,
                                    onClick = {},
                                )
                                MenuRow(
                                    title = stringResource(R.string.profile_appeal_history),
                                    onClick = { onEvent(MenuScreenEvent.AppealHistoryClick) },
                                )
                            }
                            SectionDivider()
                            EcoCard(
                                modifier = Modifier.padding(horizontal = AppSpacing.md),
                                cornerRadius = AppRadius.listCard,
                                contentPadding = PaddingValues(vertical = AppSpacing.xxs),
                            ) {
                                MenuRow(
                                    title = stringResource(R.string.action_notice),
                                    onClick = { onEvent(MenuScreenEvent.NoticeClick) },
                                )
                                MenuRow(
                                    title = stringResource(R.string.action_help),
                                    valueText = stringResource(R.string.help_later),
                                    enabled = false,
                                    onClick = { onEvent(MenuScreenEvent.HelpClick) },
                                )
                            }
                            SectionDivider()
                            if (uiState.isWithdrawing) {
                                Row(
                                    modifier = Modifier.padding(horizontal = AppSpacing.screenHorizontal),
                                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                    )
                                    Text(stringResource(R.string.withdrawal_in_progress))
                                }
                            } else {
                                Row(
                                    modifier = Modifier.padding(horizontal = AppSpacing.screenHorizontal),
                                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.lg),
                                ) {
                                    MenuTextAction(
                                        text = stringResource(R.string.action_logout),
                                        onClick = { onEvent(MenuScreenEvent.LogoutClick) },
                                    )
                                    MenuTextAction(
                                        text = stringResource(R.string.action_withdraw),
                                        isDestructive = true,
                                        onClick = { onEvent(MenuScreenEvent.WithdrawClick) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Menu", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Menu · compact larger text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun MenuScreenPreview() {
    EcoGuardTheme {
        MenuScreen(
            uiState = MenuPreviewFixtures.menuState,
            onEvent = {},
        )
    }
}
