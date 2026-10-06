package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 모집 기간·모집 인원·활동 시간을 회색 박스에 표시한다. */
@Composable
fun RecruitmentInfoTable(
    recruitment: RecruitmentUiState.Content,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.button))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
    ) {
        InfoRow(
            label = stringResource(R.string.recruitment_period),
            value = stringResource(
                R.string.format_time_range,
                recruitment.periodStartDate,
                recruitment.periodEndDate,
            ),
        )

        InfoRow(
            label = stringResource(R.string.recruitment_headcount),
            value = stringResource(
                R.string.recruitment_max_per_class,
                recruitment.maxApplicantsPerClass,
            ),
        )

        InfoRow(
            label = stringResource(R.string.common_activity_time),
            value = stringResource(
                R.string.recruitment_daily_time,
                recruitment.activityStartTime,
                recruitment.activityEndTime,
            ),
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun RecruitmentInfoTablePreview() {
    EcoGuardTheme {
        RecruitmentInfoTable(
            recruitment = RecruitmentPreviewFixtures.openRecruitment,
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
