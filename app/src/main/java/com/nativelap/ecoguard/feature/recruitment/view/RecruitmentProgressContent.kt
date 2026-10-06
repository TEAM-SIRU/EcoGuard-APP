package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState
import com.nativelap.ecoguard.ui.component.EcoProgressBar
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val recruitmentProgressSpacing = 10.dp

/** 우리 반 신청 현황 제목·인원, 진행 막대, 상태 안내 문구. 마감이면 회색으로 표시한다. */
@Composable
fun RecruitmentProgressContent(
    recruitment: RecruitmentUiState.Content,
    modifier: Modifier = Modifier,
) {
    val applicationStatus = recruitment.applicationStatus
    val isClosed = applicationStatus is RecruitmentApplicationStatus.Closed
    val progressColor = if (isClosed) {
        MaterialTheme.extraColors.captionTextColor
    } else {
        MaterialTheme.colorScheme.primary
    }
    val statusMessage = when (applicationStatus) {
        is RecruitmentApplicationStatus.Open -> stringResource(
            R.string.recruitment_spots_remaining,
            applicationStatus.remainingSpots,
        )
        RecruitmentApplicationStatus.Closed -> stringResource(
            R.string.recruitment_class_full,
            recruitment.grade,
            recruitment.classNumber,
        )
        is RecruitmentApplicationStatus.AlreadyApplied -> stringResource(
            R.string.recruitment_applied_at,
            applicationStatus.appliedDateTime,
            applicationStatus.applicationOrder,
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(recruitmentProgressSpacing),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        ) {
            Text(
                text = stringResource(
                    R.string.recruitment_application_status,
                    recruitment.grade,
                    recruitment.classNumber,
                ),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(
                    R.string.format_application_headcount,
                    recruitment.appliedCount,
                    recruitment.maxApplicantsPerClass,
                ),
                style = MaterialTheme.typography.titleMedium,
                color = progressColor,
            )
        }

        EcoProgressBar(
            currentCount = recruitment.appliedCount,
            maxCount = recruitment.maxApplicantsPerClass,
            progressColor = progressColor,
        )

        Text(
            text = statusMessage,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.extraColors.captionTextColor,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun RecruitmentProgressContentPreview() {
    EcoGuardTheme {
        RecruitmentProgressContent(
            recruitment = RecruitmentPreviewFixtures.closedRecruitment,
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
