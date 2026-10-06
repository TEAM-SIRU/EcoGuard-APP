package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityRecordUiModel
import com.nativelap.ecoguard.ui.component.StatusChip
import com.nativelap.ecoguard.ui.component.StatusChipType
import com.nativelap.ecoguard.ui.component.TwoLineTextRow
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 활동 기록 한 줄. 미제출이 아니면 눌러서 인증 상세로 이동한다. */
@Composable
fun ActivityRecordRow(
    activityRecord: ActivityRecordUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isNotSubmitted = activityRecord.status == StatusChipType.NOT_SUBMITTED
    val separator = stringResource(R.string.format_list_separator)
    val shouldShowAreaName = activityRecord.status != StatusChipType.APPROVED
    val detailParts = buildList {
        if (shouldShowAreaName) {
            add(activityRecord.areaName)
        }

        if (activityRecord.submittedTime != null) {
            add(activityRecord.submittedTime)
        }

        if (activityRecord.earnedMinutes > 0) {
            add(stringResource(R.string.format_bonus_minutes, activityRecord.earnedMinutes))
        }

        if (activityRecord.isAppealApproved) {
            add(stringResource(R.string.activity_appeal_approved))
        }

        if (isNotSubmitted) {
            add(stringResource(R.string.activity_not_verified))
        }
    }

    TwoLineTextRow(
        title = activityRecord.dateLabel,
        subtitle = detailParts.joinToString(separator = separator),
        modifier = if (isNotSubmitted) {
            modifier
        } else {
            modifier.clickable(onClick = onClick)
        },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            StatusChip(type = activityRecord.status)

            if (isNotSubmitted) {
                Spacer(modifier = Modifier.width(AppIconSize.button))
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right_20),
                    contentDescription = null,
                    modifier = Modifier.size(AppIconSize.button),
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ActivityRecordRowPreview() {
    EcoGuardTheme {
        ActivityRecordRow(
            activityRecord = ActivityPreviewFixtures.weekGroups.first().records.first(),
            onClick = {},
        )
    }
}
