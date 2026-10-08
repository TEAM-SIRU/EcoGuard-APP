package com.nativelap.ecoguard.feature.recruitment.view

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
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SectionDivider
import com.nativelap.ecoguard.ui.component.TwoLineTextRow
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun RecruitmentScreen(
    uiState: RecruitmentUiState.Content,
    onEvent: (RecruitmentScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            RecruitmentBottomAction(
                applicationStatus = uiState.applicationStatus,
                onApplyClick = { onEvent(RecruitmentScreenEvent.ApplyClick) },
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
            Column(
                modifier =
                    Modifier
                        .widthIn(max = AppComponentSize.contentMaxWidth)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                PageTitle(
                    title = stringResource(R.string.recruitment_title, uiState.semesterName),
                    subtitle =
                        stringResource(
                            R.string.recruitment_first_come_policy,
                            uiState.maxApplicantsPerClass,
                        ),
                    modifier =
                        Modifier.padding(
                            start = AppSpacing.screenHorizontal,
                            end = AppSpacing.screenHorizontal,
                            top = AppSpacing.md,
                            bottom = AppSpacing.lg,
                        ),
                )

                RecruitmentInfoTable(
                    recruitment = uiState,
                    modifier = Modifier.padding(horizontal = AppSpacing.screenHorizontal),
                )

                RecruitmentProgressContent(
                    recruitment = uiState,
                    modifier = Modifier.padding(AppSpacing.xl),
                )

                SectionDivider()

                TwoLineTextRow(
                    title =
                        stringResource(
                            R.string.recruitment_volunteer_time_per_verification,
                            uiState.volunteerMinutesPerVerification,
                        ),
                    subtitle = stringResource(R.string.recruitment_approved_activity_record),
                )

                TwoLineTextRow(
                    title = stringResource(R.string.recruitment_simple_photo_verification),
                    subtitle = stringResource(R.string.recruitment_take_photo_to_submit),
                )
            }
        }
    }
}

@Composable
private fun RecruitmentBottomAction(
    applicationStatus: RecruitmentApplicationStatus,
    onApplyClick: () -> Unit,
) {
    when (applicationStatus) {
        is RecruitmentApplicationStatus.Open -> {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.recruitment_apply),
                    onClick = onApplyClick,
                )
            }
        }

        RecruitmentApplicationStatus.Closed -> {
            BottomCtaBar {
                EcoSecondaryButton(
                    text = stringResource(R.string.recruitment_closed),
                    onClick = {},
                    enabled = false,
                )
            }
        }

        is RecruitmentApplicationStatus.AlreadyApplied -> {
            Unit
        }
    }
}

@Preview(name = "Recruitment · open", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(
    name = "Recruitment · compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Composable
private fun RecruitmentScreenOpenPreview() {
    EcoGuardTheme {
        RecruitmentScreen(
            uiState = RecruitmentPreviewFixtures.openRecruitment,
            onEvent = {},
        )
    }
}

@Preview(name = "Recruitment · closed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RecruitmentScreenClosedPreview() {
    EcoGuardTheme {
        RecruitmentScreen(
            uiState = RecruitmentPreviewFixtures.closedRecruitment,
            onEvent = {},
        )
    }
}

@Preview(name = "Recruitment · already applied", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RecruitmentScreenAppliedPreview() {
    EcoGuardTheme {
        RecruitmentScreen(
            uiState = RecruitmentPreviewFixtures.appliedRecruitment,
            onEvent = {},
        )
    }
}
