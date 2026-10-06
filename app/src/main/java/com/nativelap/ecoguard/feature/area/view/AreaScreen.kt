package com.nativelap.ecoguard.feature.area.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.EcoSegmentedControl
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SectionDivider
import com.nativelap.ecoguard.ui.component.TwoLineTextRow
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

// Figma의 도면 영역 높이(308)에서 상하 여백(4·24)을 뺀 값. 도면 이미지는 서버 연동 시 채운다.
private val floorPlanHeight = 280.dp

private val areaTitlePadding = PaddingValues(
    start = AppSpacing.screenHorizontal,
    end = AppSpacing.screenHorizontal,
    top = AppSpacing.xs,
    bottom = AppSpacing.lg,
)

@Composable
fun AreaScreen(
    uiState: AreaUiState,
    onEvent: (AreaScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.AREA,
                onTabSelected = { selectedTab -> onEvent(AreaScreenEvent.TabSelect(selectedTab)) },
                onCameraClick = { onEvent(AreaScreenEvent.CameraClick) },
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
                is AreaUiState.Content -> Column(
                    modifier = Modifier
                        .widthIn(max = AppComponentSize.contentMaxWidth)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                ) {
                    PageTitle(
                        title = stringResource(R.string.common_my_cleaning_area),
                        subtitle = stringResource(R.string.area_selected_floor, uiState.selectedFloor),
                        modifier = Modifier.padding(areaTitlePadding),
                    )

                    AreaDetailContent(
                        areaContent = uiState,
                        onFloorSelect = { selectedFloor -> onEvent(AreaScreenEvent.FloorSelect(selectedFloor)) },
                    )
                }
                AreaUiState.NotAssigned -> AreaEmptyLayout {
                    InlineEmptyState(
                        iconRes = R.drawable.ic_map_30,
                        title = stringResource(R.string.area_not_assigned_title),
                        description = stringResource(R.string.area_not_assigned_description),
                    )
                }
                AreaUiState.LoadFailed -> AreaEmptyLayout {
                    InlineEmptyState(
                        iconRes = R.drawable.ic_map_30,
                        title = stringResource(R.string.area_load_failed),
                        description = stringResource(R.string.area_load_failed_description),
                        actionText = stringResource(R.string.action_retry),
                        onActionClick = { onEvent(AreaScreenEvent.RetryClick) },
                    )
                }
                AreaUiState.Loading -> Unit
            }
        }
    }
}

@Composable
private fun AreaEmptyLayout(
    emptyContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .widthIn(max = AppComponentSize.contentMaxWidth)
            .fillMaxSize(),
    ) {
        PageTitle(
            title = stringResource(R.string.common_my_cleaning_area),
            modifier = Modifier.padding(areaTitlePadding),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppSpacing.screenHorizontal,
                    vertical = AppSpacing.xl,
                ),
            contentAlignment = Alignment.Center,
        ) {
            emptyContent()
        }
    }
}

@Composable
private fun AreaDetailContent(
    areaContent: AreaUiState.Content,
    onFloorSelect: (Int) -> Unit,
) {
    val floorLabels = areaContent.floors.map { floor ->
        stringResource(R.string.format_floor, floor)
    }
    val memberSeparator = stringResource(R.string.format_list_separator)
    val memberNames = (areaContent.teammateNames + stringResource(R.string.area_member_me, areaContent.myName))
        .joinToString(separator = memberSeparator)

    Column {
        EcoSegmentedControl(
            segmentLabels = floorLabels,
            selectedIndex = areaContent.floors
                .indexOf(areaContent.selectedFloor)
                .coerceAtLeast(0),
            onSegmentSelect = { selectedIndex -> onFloorSelect(areaContent.floors[selectedIndex]) },
            modifier = Modifier.padding(
                start = AppSpacing.screenHorizontal,
                end = AppSpacing.screenHorizontal,
                bottom = AppSpacing.md,
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppSpacing.screenHorizontal,
                    end = AppSpacing.screenHorizontal,
                    top = AppSpacing.xxs,
                    bottom = AppSpacing.xl,
                )
                .height(floorPlanHeight),
        )

        SectionDivider()

        TwoLineTextRow(
            title = areaContent.areaName,
            subtitle = stringResource(R.string.area_description_format, areaContent.areaDescription),
        )

        TwoLineTextRow(
            title = stringResource(
                R.string.recruitment_daily_time,
                areaContent.cleaningStartTime,
                areaContent.cleaningEndTime,
            ),
            subtitle = stringResource(R.string.home_cleaning_time),
        )

        TwoLineTextRow(
            title = stringResource(R.string.area_member_count, areaContent.teammateNames.size + 1),
            subtitle = memberNames,
        )
    }
}

@Preview(name = "Area · content", showBackground = true, widthDp = 390, heightDp = 895)
@Composable
private fun AreaScreenContentPreview() {
    EcoGuardTheme {
        AreaScreen(
            uiState = AreaPreviewFixtures.areaContent,
            onEvent = {},
        )
    }
}

@Preview(name = "Area · not assigned", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AreaScreenNotAssignedPreview() {
    EcoGuardTheme {
        AreaScreen(
            uiState = AreaUiState.NotAssigned,
            onEvent = {},
        )
    }
}

@Preview(name = "Area · load failed", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Area · load failed compact larger text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun AreaScreenLoadFailedPreview() {
    EcoGuardTheme {
        AreaScreen(
            uiState = AreaUiState.LoadFailed,
            onEvent = {},
        )
    }
}
