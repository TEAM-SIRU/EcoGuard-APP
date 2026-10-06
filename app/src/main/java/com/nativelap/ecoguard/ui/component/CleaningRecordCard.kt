package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val recordTextSpacing = 2.dp

/** 청소 기록 한 건. 제출 일시·구역, 인증 상태 칩과 적립 시간을 표시한다. */
@Composable
fun CleaningRecordCard(
    submittedDateTime: String,
    areaName: String,
    status: StatusChipType,
    earnedMinutes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val earnedTimeText = if (earnedMinutes > 0) {
        stringResource(R.string.format_bonus_minutes, earnedMinutes)
    } else {
        stringResource(R.string.format_minutes, earnedMinutes)
    }
    val earnedTimeColor = if (earnedMinutes > 0) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.extraColors.disabledContentColor
    }

    EcoCard(
        modifier = modifier,
        onClick = onClick,
        cornerRadius = AppRadius.button,
        contentPadding = PaddingValues(
            horizontal = AppSpacing.lg,
            vertical = AppSpacing.md,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(recordTextSpacing),
            ) {
                Text(
                    text = submittedDateTime,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = areaName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.extraColors.captionTextColor,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(recordTextSpacing),
            ) {
                StatusChip(
                    type = status,
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                )

                Text(
                    text = earnedTimeText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = earnedTimeColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390)
@Composable
private fun CleaningRecordCardPreview() {
    EcoGuardTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            CleaningRecordCard(
                submittedDateTime = "9월 28일(월) 08:05",
                areaName = "본관 2층 복도 A",
                status = StatusChipType.APPROVED,
                earnedMinutes = 10,
                onClick = {},
            )

            CleaningRecordCard(
                submittedDateTime = "9월 22일(화) 08:04",
                areaName = "본관 2층 복도 A",
                status = StatusChipType.REJECTED,
                earnedMinutes = 0,
                onClick = {},
            )
        }
    }
}
