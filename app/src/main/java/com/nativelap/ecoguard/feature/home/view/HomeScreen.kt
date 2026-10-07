package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.EcoTopBar
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val homeContentBottomPadding = 32.dp

@Composable
fun HomeScreen(
    uiState: HomeUiState.Content,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EcoTopBar(
                title = stringResource(R.string.brand_name),
                onActionClick = { onEvent(HomeScreenEvent.NoticeIconClick) },
            )
        },
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.HOME,
                onTabSelected = { selectedTab -> onEvent(HomeScreenEvent.TabSelect(selectedTab)) },
                onCameraClick = { onEvent(HomeScreenEvent.CleaningVerificationClick) },
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
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal,
                        top = AppSpacing.xs,
                        bottom = homeContentBottomPadding,
                    ),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            ) {
                if (uiState.notice != null) {
                    HomeNoticeCard(
                        notice = uiState.notice,
                        onCloseClick = { onEvent(HomeScreenEvent.NoticeCloseClick) },
                        onDetailClick = { onEvent(HomeScreenEvent.NoticeDetailClick) },
                    )
                }

                HomeSection(
                    section = uiState.section,
                    onEvent = onEvent,
                )
            }
        }
    }
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
            HomeTodayCleaningCard(
                todayCleaning = section.todayCleaning,
                onVerificationClick = { onEvent(HomeScreenEvent.CleaningVerificationClick) },
                onSubmittedPhotoClick = { onEvent(HomeScreenEvent.SubmittedPhotoClick) },
                onActivityRecordClick = { onEvent(HomeScreenEvent.ActivityRecordClick) },
                onAppealClick = { onEvent(HomeScreenEvent.AppealClick) },
            )

            HomeWeekCleaningCard(weekCleaning = section.weekCleaning)

            HomeRecentRecordsContent(
                recentRecords = section.recentRecords,
                onAllRecordsClick = { onEvent(HomeScreenEvent.AllRecordsClick) },
                onRecordClick = { recordId -> onEvent(HomeScreenEvent.RecordClick(recordId)) },
                modifier = Modifier.padding(top = AppSpacing.sm),
            )
        }
    }
}

@Preview(name = "Home · not submitted", showBackground = true, widthDp = 390, heightDp = 1230)
@Preview(name = "Home · compact larger text", showBackground = true, widthDp = 320, heightDp = 900, fontScale = 1.5f)
@Preview(name = "Home · tablet", showBackground = true, widthDp = 840, heightDp = 1230)
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
