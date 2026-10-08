package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.AdaptiveButtonRow
import com.nativelap.ecoguard.ui.component.EcoDialogButton
import com.nativelap.ecoguard.ui.component.EcoDialogSurface
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private const val MONTHS_PER_ROW = 3
private const val MONTH_COUNT = 12
private val monthItemHeight = 48.dp

/** 활동 기록 조회 월 선택 팝업. 연도 이동과 1–12월 선택 후 적용한다. */
@Composable
fun MonthPickerDialog(
    initialYear: Int,
    initialMonth: Int,
    onDismissRequest: () -> Unit,
    onApply: (year: Int, month: Int) -> Unit,
) {
    var selectedYear by rememberSaveable { mutableIntStateOf(initialYear) }
    var selectedMonth by rememberSaveable { mutableIntStateOf(initialMonth) }

    EcoDialogSurface(onDismissRequest = onDismissRequest) {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Text(
                text = stringResource(R.string.activity_select_month),
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.activity_select_month_description),
                style = MaterialTheme.extraTypography.loadingStatus,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { selectedYear -= 1 },
                modifier = Modifier.size(AppComponentSize.minTouchTarget),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.cd_previous_year),
                    modifier = Modifier.size(AppIconSize.button),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                text = stringResource(R.string.format_year, selectedYear),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            IconButton(
                onClick = { selectedYear += 1 },
                modifier = Modifier.size(AppComponentSize.minTouchTarget),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right_20),
                    contentDescription = stringResource(R.string.cd_next_year),
                    modifier = Modifier.size(AppIconSize.button),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            (1..MONTH_COUNT).chunked(MONTHS_PER_ROW).forEach { monthRow ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                    monthRow.forEach { month ->
                        MonthItem(
                            month = month,
                            isSelected = month == selectedMonth,
                            onClick = { selectedMonth = month },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        AdaptiveButtonRow(
            firstButton = { itemModifier ->
                EcoDialogButton(
                    text = stringResource(R.string.action_cancel),
                    onClick = onDismissRequest,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = itemModifier,
                )
            },
            secondButton = { itemModifier ->
                EcoDialogButton(
                    text = stringResource(R.string.action_apply),
                    onClick = { onApply(selectedYear, selectedMonth) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = itemModifier,
                )
            },
        )
    }
}

@Composable
private fun MonthItem(
    month: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .heightIn(min = monthItemHeight)
                .clip(RoundedCornerShape(AppRadius.button))
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                ).selectable(
                    selected = isSelected,
                    onClick = onClick,
                    role = Role.RadioButton,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.format_month, month),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
        )
    }
}

@Preview(name = "Month picker", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MonthPickerDialogPreview() {
    EcoGuardTheme {
        MonthPickerDialog(
            initialYear = 2026,
            initialMonth = 9,
            onDismissRequest = {},
            onApply = { _, _ -> },
        )
    }
}
