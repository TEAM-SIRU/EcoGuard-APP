package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.MenuRow
import com.nativelap.ecoguard.ui.component.SectionDivider
import com.nativelap.ecoguard.ui.component.StatSummaryCard
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 12 전체(마이페이지). 프로필, 이번 달 요약, 메뉴 목록, 로그아웃. */
@Composable
fun MenuScreen(
    uiState: MenuUiState,
    onEvent: (MenuScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.MY_PAGE,
                onTabSelected = { selectedTab -> onEvent(MenuScreenEvent.TabSelect(selectedTab)) },
                onCameraClick = { onEvent(MenuScreenEvent.CameraClick) },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                MenuProfileHeader(
                    menuState = uiState,
                    modifier = Modifier.padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal,
                        top = AppSpacing.md,
                        bottom = AppSpacing.lg,
                    ),
                )

                Row(
                    modifier = Modifier.padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal,
                        bottom = AppSpacing.lg,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                ) {
                    StatSummaryCard(
                        label = stringResource(R.string.profile_monthly_approvals),
                        value = stringResource(R.string.format_times, uiState.monthlyApprovalCount),
                        modifier = Modifier.weight(1f),
                    )

                    StatSummaryCard(
                        label = stringResource(R.string.profile_activity_time),
                        value = stringResource(R.string.format_minutes, uiState.monthlyMinutes),
                        modifier = Modifier.weight(1f),
                    )
                }

                SectionDivider()

                MenuRow(
                    title = stringResource(R.string.common_my_cleaning_area),
                    valueText = uiState.assignedAreaName,
                    onClick = { onEvent(MenuScreenEvent.AreaClick) },
                )

                MenuRow(
                    title = stringResource(R.string.profile_application_result),
                    valueText = if (uiState.isApplicationCompleted) {
                        stringResource(R.string.home_application_completed)
                    } else {
                        null
                    },
                    onClick = { onEvent(MenuScreenEvent.ApplicationResultClick) },
                )

                MenuRow(
                    title = stringResource(R.string.profile_cleaning_notification),
                    onClick = {
                        onEvent(MenuScreenEvent.CleaningNotificationToggle(!uiState.isCleaningNotificationEnabled))
                    },
                ) {
                    Switch(
                        checked = uiState.isCleaningNotificationEnabled,
                        onCheckedChange = null,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }

                MenuRow(
                    title = stringResource(R.string.profile_appeal_history),
                    onClick = { onEvent(MenuScreenEvent.AppealHistoryClick) },
                )

                SectionDivider()

                MenuRow(
                    title = stringResource(R.string.action_notice),
                    onClick = { onEvent(MenuScreenEvent.NoticeClick) },
                )

                MenuRow(
                    title = stringResource(R.string.action_help),
                    onClick = { onEvent(MenuScreenEvent.HelpClick) },
                )

                SectionDivider()

                Text(
                    text = stringResource(R.string.action_logout),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = AppComponentSize.minTouchTarget)
                        .clickable(
                            role = Role.Button,
                            onClick = { onEvent(MenuScreenEvent.LogoutClick) },
                        )
                        .padding(
                            horizontal = AppSpacing.screenHorizontal,
                            vertical = AppSpacing.md,
                        ),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.extraColors.captionTextColor,
                )
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
