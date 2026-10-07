package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityWeekGroupUiModel
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SectionDivider
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val activityTitleModifier = Modifier.padding(
    start = AppSpacing.screenHorizontal,
    end = AppSpacing.screenHorizontal,
    top = AppSpacing.md,
    bottom = AppSpacing.lg,
)

/** 07 활동 기록. 월별 요약과 주 단위 기록 목록, 빈 상태·조회 실패를 표시한다. */
@Composable
fun ActivityScreen(
    uiState: ActivityUiState,
    onEvent: (ActivityScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.ACTIVITY,
                onTabSelected = { selectedTab -> onEvent(ActivityScreenEvent.TabSelect(selectedTab)) },
                onCameraClick = { onEvent(ActivityScreenEvent.CameraClick) },
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
            when (uiState) {
                is ActivityUiState.Content -> if (uiState.weekGroups.isEmpty()) {
                    ActivityEmptyLayout(
                        headerContent = {
                            ActivitySummaryContent(
                                activityContent = uiState,
                                onMonthSelectorClick = { onEvent(ActivityScreenEvent.MonthSelectorClick) },
                            )

                            SectionDivider()
                        },
                        emptyContent = {
                            InlineEmptyState(
                                iconRes = R.drawable.ic_list_30,
                                title = stringResource(R.string.activity_empty_title),
                                description = stringResource(R.string.activity_empty_description),
                                actionText = stringResource(R.string.activity_start_verification),
                                onActionClick = { onEvent(ActivityScreenEvent.StartVerificationClick) },
                            )
                        },
                    )
                } else {
                    ActivityRecordList(
                        activityContent = uiState,
                        onEvent = onEvent,
                    )
                }
                ActivityUiState.LoadFailed -> ActivityEmptyLayout(
                    headerContent = {},
                ) {
                    InlineEmptyState(
                        iconRes = R.drawable.ic_map_30,
                        title = stringResource(R.string.activity_load_failed),
                        description = stringResource(R.string.common_try_again_later),
                        actionText = stringResource(R.string.action_retry),
                        onActionClick = { onEvent(ActivityScreenEvent.RetryClick) },
                    )
                }
                ActivityUiState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun ActivityRecordList(
    activityContent: ActivityUiState.Content,
    onEvent: (ActivityScreenEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .widthIn(max = AppComponentSize.contentMaxWidth)
            .fillMaxWidth(),
        contentPadding = PaddingValues(bottom = AppComponentSize.cameraFabScrollClearance),
    ) {
        item {
            PageTitle(
                title = stringResource(R.string.activity_title),
                modifier = activityTitleModifier,
            )
        }

        item {
            ActivitySummaryContent(
                activityContent = activityContent,
                onMonthSelectorClick = { onEvent(ActivityScreenEvent.MonthSelectorClick) },
            )
        }

        item {
            SectionDivider()
        }

        activityContent.weekGroups.forEachIndexed { groupIndex, weekGroup ->
            item(key = "week-${weekGroup.weeksAgo}") {
                ActivityWeekHeader(
                    weekGroup = weekGroup,
                    isFirstGroup = groupIndex == 0,
                )
            }

            items(
                items = weekGroup.records,
                key = { activityRecord -> activityRecord.recordId },
            ) { activityRecord ->
                ActivityRecordRow(
                    activityRecord = activityRecord,
                    onClick = { onEvent(ActivityScreenEvent.RecordClick(activityRecord.recordId)) },
                )
            }
        }
    }
}

@Composable
private fun ActivityWeekHeader(
    weekGroup: ActivityWeekGroupUiModel,
    isFirstGroup: Boolean,
) {
    val weekLabel = when (weekGroup.weeksAgo) {
        0 -> stringResource(R.string.activity_this_week)
        1 -> stringResource(R.string.activity_last_week)
        else -> stringResource(R.string.activity_weeks_ago, weekGroup.weeksAgo)
    }
    val topPadding = if (isFirstGroup) {
        AppSpacing.md
    } else {
        AppSpacing.lg
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = weekLabel,
            modifier = Modifier.padding(
                start = AppSpacing.screenHorizontal,
                end = AppSpacing.screenHorizontal,
                top = topPadding,
            ),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.extraColors.captionTextColor,
        )

        weekGroup.holidayRangeTexts.forEach { holidayRangeText ->
            Text(
                text = stringResource(R.string.activity_holiday_format, holidayRangeText),
                modifier = Modifier.padding(
                    horizontal = AppSpacing.screenHorizontal,
                    vertical = AppSpacing.sm,
                ),
                style = MaterialTheme.extraTypography.captionRegular,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}

@Composable
private fun ActivityEmptyLayout(
    headerContent: @Composable ColumnScope.() -> Unit,
    emptyContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .widthIn(max = AppComponentSize.contentMaxWidth)
            .fillMaxSize(),
    ) {
        PageTitle(
            title = stringResource(R.string.activity_title),
            modifier = activityTitleModifier,
        )

        headerContent()

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = AppSpacing.screenHorizontal,
                    end = AppSpacing.screenHorizontal,
                    top = AppSpacing.xl,
                    // 빈 상태 버튼이 탭 바 위로 튀어나온 카메라 버튼에 가리지 않게 한다.
                    bottom = AppComponentSize.cameraFabScrollClearance,
                ),
            contentAlignment = Alignment.Center,
        ) {
            emptyContent()
        }
    }
}

@Preview(name = "Activity · content", showBackground = true, widthDp = 390, heightDp = 1372)
@Preview(name = "Activity · compact larger text", showBackground = true, widthDp = 320, heightDp = 900, fontScale = 1.5f)
@Composable
private fun ActivityScreenPreview() {
    EcoGuardTheme {
        ActivityScreen(
            uiState = ActivityPreviewFixtures.activityContent,
            onEvent = {},
        )
    }
}

@Preview(name = "Activity · empty", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ActivityScreenEmptyPreview() {
    EcoGuardTheme {
        ActivityScreen(
            uiState = ActivityPreviewFixtures.emptyContent,
            onEvent = {},
        )
    }
}

@Preview(name = "Activity · load failed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ActivityScreenLoadFailedPreview() {
    EcoGuardTheme {
        ActivityScreen(
            uiState = ActivityUiState.LoadFailed,
            onEvent = {},
        )
    }
}
