package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.nativelap.ecoguard.feature.home.viewmodel.HomeNoticeUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningUiModel
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.EcoTopBar
import com.nativelap.ecoguard.ui.component.contentColumnWidth
import com.nativelap.ecoguard.ui.component.isExpandedLayout
import com.nativelap.ecoguard.ui.component.twoPaneContentMaxWidth
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState.Content,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val section = uiState.section
    val isTwoPane = isExpandedLayout() && section is HomeSectionUiModel.Cleaning
    val contentMaxWidth = if (isTwoPane) {
        twoPaneContentMaxWidth()
    } else {
        AppComponentSize.contentMaxWidth
    }
    // 2열·1열이 바뀌어도(회전, 멀티 윈도우) 스크롤 위치를 유지하도록 두 배치가 같은 상태를 쓴다.
    val homeScrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EcoTopBar(
                title = stringResource(R.string.brand_name),
                onActionClick = { onEvent(HomeScreenEvent.NoticeIconClick) },
                contentMaxWidth = contentMaxWidth,
            )
        },
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.HOME,
                onTabSelected = { selectedTab -> onEvent(HomeScreenEvent.TabSelect(selectedTab)) },
                onCameraClick = { onEvent(HomeScreenEvent.CleaningVerificationClick) },
                isCameraEnabled = uiState.isCameraVerificationAvailable,
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
            if (isTwoPane && section is HomeSectionUiModel.Cleaning) {
                HomeTwoPaneCleaningLayout(
                    notice = uiState.notice,
                    cleaningSection = section,
                    scrollState = homeScrollState,
                    onEvent = onEvent,
                    modifier = Modifier.contentColumnWidth(contentMaxWidth),
                )
            } else {
                Column(
                    modifier = Modifier
                        .contentColumnWidth()
                        .verticalScroll(homeScrollState)
                        .padding(homeContentPadding),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                ) {
                    HomeNoticeSlot(
                        notice = uiState.notice,
                        onEvent = onEvent,
                    )

                    HomeSection(
                        section = section,
                        onEvent = onEvent,
                    )
                }
            }
        }
    }
}

private val homeContentPadding = PaddingValues(
    start = AppSpacing.screenHorizontal,
    end = AppSpacing.screenHorizontal,
    top = AppSpacing.xs,
    bottom = AppComponentSize.cameraFabScrollClearance,
)

// 넓은 화면에서는 공지·오늘의 청소를 왼쪽, 이번 주 청소·최근 기록을 오른쪽 열에 둔다.
@Composable
private fun HomeTwoPaneCleaningLayout(
    notice: HomeNoticeUiModel?,
    cleaningSection: HomeSectionUiModel.Cleaning,
    scrollState: ScrollState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(homeContentPadding),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xl),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            HomeNoticeSlot(
                notice = notice,
                onEvent = onEvent,
            )

            HomeTodayCleaningSlot(
                todayCleaning = cleaningSection.todayCleaning,
                onEvent = onEvent,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            HomeWeekCleaningCard(weekCleaning = cleaningSection.weekCleaning)

            HomeRecentRecordsSlot(
                cleaningSection = cleaningSection,
                onEvent = onEvent,
                modifier = Modifier.padding(top = AppSpacing.sm),
            )
        }
    }
}

@Composable
private fun HomeNoticeSlot(
    notice: HomeNoticeUiModel?,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    if (notice != null) {
        HomeNoticeCard(
            notice = notice,
            onCloseClick = { onEvent(HomeScreenEvent.NoticeCloseClick) },
            onDetailClick = { onEvent(HomeScreenEvent.NoticeDetailClick) },
        )
    }
}

@Composable
private fun HomeTodayCleaningSlot(
    todayCleaning: TodayCleaningUiModel,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    HomeTodayCleaningCard(
        todayCleaning = todayCleaning,
        onVerificationClick = { onEvent(HomeScreenEvent.CleaningVerificationClick) },
        onSubmittedPhotoClick = { onEvent(HomeScreenEvent.SubmittedPhotoClick) },
        onActivityRecordClick = { onEvent(HomeScreenEvent.ActivityRecordClick) },
        onAppealClick = { onEvent(HomeScreenEvent.AppealClick) },
    )
}

@Composable
private fun HomeRecentRecordsSlot(
    cleaningSection: HomeSectionUiModel.Cleaning,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeRecentRecordsContent(
        recentRecords = cleaningSection.recentRecords,
        onAllRecordsClick = { onEvent(HomeScreenEvent.AllRecordsClick) },
        onRecordClick = { recordId -> onEvent(HomeScreenEvent.RecordClick(recordId)) },
        modifier = modifier,
    )
}

@Composable
private fun HomeSection(
    section: HomeSectionUiModel,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    when (section) {
        is HomeSectionUiModel.Recruiting -> HomeRecruitmentCard(
            recruiting = section,
            onRecruitmentClick = { onEvent(HomeScreenEvent.RecruitmentClick) },
        )
        HomeSectionUiModel.WaitingAssignment -> HomeApplicationCompletedCard(
            onApplicationResultClick = { onEvent(HomeScreenEvent.ApplicationResultClick) },
        )
        is HomeSectionUiModel.Cleaning -> {
            HomeTodayCleaningSlot(
                todayCleaning = section.todayCleaning,
                onEvent = onEvent,
            )

            HomeWeekCleaningCard(weekCleaning = section.weekCleaning)

            HomeRecentRecordsSlot(
                cleaningSection = section,
                onEvent = onEvent,
                modifier = Modifier.padding(top = AppSpacing.sm),
            )
        }
    }
}

@Preview(name = "Home · not submitted", showBackground = true, widthDp = 390, heightDp = 1230)
@Preview(name = "Home · compact larger text", showBackground = true, widthDp = 320, heightDp = 900, fontScale = 1.5f)
@Preview(name = "Home · tablet two-pane", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun HomeScreenNotSubmittedPreview() {
    EcoGuardTheme {
        HomeScreen(
            uiState = HomePreviewFixtures.notSubmittedHome,
            onEvent = {},
        )
    }
}

@Preview(name = "Home · rejected", showBackground = true, widthDp = 390, heightDp = 1035)
@Composable
private fun HomeScreenRejectedPreview() {
    EcoGuardTheme {
        HomeScreen(
            uiState = HomePreviewFixtures.rejectedHome,
            onEvent = {},
        )
    }
}

@Preview(name = "Home · recruiting", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenRecruitingPreview() {
    EcoGuardTheme {
        HomeScreen(
            uiState = HomePreviewFixtures.recruitingHome,
            onEvent = {},
        )
    }
}

@Preview(name = "Home · waiting assignment", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenWaitingAssignmentPreview() {
    EcoGuardTheme {
        HomeScreen(
            uiState = HomePreviewFixtures.waitingAssignmentHome,
            onEvent = {},
        )
    }
}
