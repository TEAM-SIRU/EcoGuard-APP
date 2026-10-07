package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.ui.component.StatSummaryItem
import com.nativelap.ecoguard.ui.component.StatSummaryRow
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val monthlyTimeSpacing = 2.dp

/** 조회 월 선택, 이번 달 활동 시간, 승인·반려·미제출 통계 카드. */
@Composable
fun ActivitySummaryContent(
    activityContent: ActivityUiState.Content,
    onMonthSelectorClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val monthlyMinutesColor = if (activityContent.monthlyMinutes > 0) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = AppSpacing.xs)
                .heightIn(min = AppComponentSize.minTouchTarget)
                .clickable(
                    role = Role.Button,
                    onClick = onMonthSelectorClick,
                )
                .padding(horizontal = AppSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.format_year_month, activityContent.year, activityContent.month),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Icon(
                painter = painterResource(R.drawable.ic_chevron_down),
                contentDescription = stringResource(R.string.activity_select_month),
                modifier = Modifier.size(AppIconSize.button),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Column(
            modifier = Modifier.padding(
                start = AppSpacing.screenHorizontal,
                end = AppSpacing.screenHorizontal,
                top = AppSpacing.xxs,
                bottom = AppSpacing.md,
            ),
            verticalArrangement = Arrangement.spacedBy(monthlyTimeSpacing),
        ) {
            Text(
                text = stringResource(R.string.activity_monthly_time),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = stringResource(R.string.format_minutes, activityContent.monthlyMinutes),
                style = MaterialTheme.typography.headlineLarge,
                color = monthlyMinutesColor,
            )
        }

        StatSummaryRow(
            statItems = listOf(
                StatSummaryItem(
                    label = stringResource(R.string.verification_status_approved),
                    value = stringResource(R.string.format_times, activityContent.approvedCount),
                ),
                StatSummaryItem(
                    label = stringResource(R.string.verification_status_rejected),
                    value = stringResource(R.string.format_times, activityContent.rejectedCount),
                ),
                StatSummaryItem(
                    label = stringResource(R.string.verification_status_not_submitted),
                    value = stringResource(R.string.format_times, activityContent.notSubmittedCount),
                ),
            ),
            modifier = Modifier.padding(
                start = AppSpacing.screenHorizontal,
                end = AppSpacing.screenHorizontal,
                bottom = AppSpacing.xl,
            ),
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ActivitySummaryContentPreview() {
    EcoGuardTheme {
        ActivitySummaryContent(
            activityContent = ActivityPreviewFixtures.activityContent,
            onMonthSelectorClick = {},
        )
    }
}
