package com.nativelap.ecoguard.feature.area.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.EcoSegmentedControl
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SectionDivider
import com.nativelap.ecoguard.ui.component.isExpandedLayout
import com.nativelap.ecoguard.ui.component.twoPaneContentMaxWidth
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val areaTitlePadding =
    PaddingValues(
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
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.AREA,
                onTabSelected = { selectedTab -> onEvent(AreaScreenEvent.TabSelect(selectedTab)) },
                onCameraClick = { onEvent(AreaScreenEvent.CameraClick) },
            )
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
            when (uiState) {
                is AreaUiState.Content -> {
                    Column(
                        modifier =
                            Modifier
                                .widthIn(max = twoPaneContentMaxWidth())
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = AppComponentSize.cameraFabScrollClearance),
                    ) {
                        PageTitle(
                            title = stringResource(R.string.common_my_cleaning_area),
                            subtitle = stringResource(R.string.area_floor_plan_description),
                            modifier = Modifier.padding(areaTitlePadding),
                        )

                        AreaDetailContent(
                            areaContent = uiState,
                            onFloorSelect = { selectedFloor -> onEvent(AreaScreenEvent.FloorSelect(selectedFloor)) },
                        )
                    }
                }

                AreaUiState.NotAssigned -> {
                    AreaEmptyLayout {
                        InlineEmptyState(
                            iconRes = R.drawable.ic_map_30,
                            title = stringResource(R.string.area_not_assigned_title),
                            description = stringResource(R.string.area_not_assigned_description),
                        )
                    }
                }

                AreaUiState.LoadFailed -> {
                    AreaEmptyLayout {
                        InlineEmptyState(
                            iconRes = R.drawable.ic_map_30,
                            title = stringResource(R.string.area_load_failed),
                            description = stringResource(R.string.area_load_failed_description),
                            actionText = stringResource(R.string.action_retry),
                            onActionClick = { onEvent(AreaScreenEvent.RetryClick) },
                        )
                    }
                }

                AreaUiState.Loading -> {
                    Unit
                }
            }
        }
    }
}

@Composable
private fun AreaEmptyLayout(emptyContent: @Composable () -> Unit) {
    Column(
        modifier =
            Modifier
                .widthIn(max = twoPaneContentMaxWidth())
                .fillMaxSize(),
    ) {
        PageTitle(
            title = stringResource(R.string.common_my_cleaning_area),
            modifier = Modifier.padding(areaTitlePadding),
        )

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal,
                        top = AppSpacing.xl,
                        // 빈 상태 버튼이 탭 바 위로 튀어나온 카메라 버튼에 가리지 않게 한다.
                        bottom = AppComponentSize.emptyStateBottomClearance,
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
    val floorLabels =
        areaContent.floors.map { floor ->
            stringResource(R.string.format_floor, floor)
        }
    val memberSeparator = stringResource(R.string.format_list_separator)
    val memberNames =
        (areaContent.teammateNames + stringResource(R.string.area_member_me, areaContent.myName))
            .joinToString(separator = memberSeparator)

    Column {
        EcoSegmentedControl(
            segmentLabels = floorLabels,
            selectedIndex =
                areaContent.floors
                    .indexOf(areaContent.selectedFloor)
                    .coerceAtLeast(0),
            onSegmentSelect = { selectedIndex -> onFloorSelect(areaContent.floors[selectedIndex]) },
            modifier =
                Modifier.padding(
                    start = AppSpacing.screenHorizontal,
                    end = AppSpacing.screenHorizontal,
                    bottom = AppSpacing.md,
                ),
        )

        if (isExpandedLayout()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xl),
                modifier = Modifier.padding(horizontal = AppSpacing.screenHorizontal),
            ) {
                AreaFloorPlan(modifier = Modifier.weight(1f))
                AreaAssignmentCard(areaContent = areaContent, memberNames = memberNames, modifier = Modifier.weight(1f))
            }
        } else {
            AreaFloorPlan(
                modifier = Modifier.padding(horizontal = AppSpacing.screenHorizontal, vertical = AppSpacing.md),
            )
            SectionDivider()
            AreaAssignmentCard(
                areaContent = areaContent,
                memberNames = memberNames,
                modifier = Modifier.padding(horizontal = AppSpacing.md),
            )
        }
    }
}

@Preview(name = "Area · content", showBackground = true, widthDp = 390, heightDp = 844)
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
@Preview(
    name = "Area · load failed compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Composable
private fun AreaScreenLoadFailedPreview() {
    EcoGuardTheme {
        AreaScreen(
            uiState = AreaUiState.LoadFailed,
            onEvent = {},
        )
    }
}
