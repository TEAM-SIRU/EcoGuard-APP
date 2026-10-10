package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.WeekCleaningUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.WeekDayUiModel
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography
import java.time.format.TextStyle
import java.util.Locale

private val weekDayLabelSpacing = 6.dp
private val todayIndicatorBorderWidth = 2.dp
private val upcomingIndicatorBorderWidth = 1.dp

@Composable
fun HomeWeekCleaningCard(
    weekCleaning: WeekCleaningUiModel,
    modifier: Modifier = Modifier,
) {
    EcoCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.home_weekly_cleaning),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text =
                    stringResource(
                        R.string.format_cleaning_days,
                        weekCleaning.completedDayCount,
                        weekCleaning.totalDayCount,
                    ),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            weekCleaning.days.forEach { weekDay ->
                WeekDayIndicator(weekDay = weekDay)
            }
        }
    }
}

@Composable
private fun WeekDayIndicator(weekDay: WeekDayUiModel) {
    val dayName = weekDay.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
    val dayLabel =
        if (weekDay.isToday) {
            stringResource(R.string.home_today)
        } else {
            dayName
        }
    val statusDescription =
        if (weekDay.isCompleted) {
            stringResource(R.string.verification_status_approved)
        } else {
            stringResource(R.string.verification_status_not_submitted)
        }

    Column(
        modifier =
            Modifier.clearAndSetSemantics {
                contentDescription = "$dayLabel $statusDescription"
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(weekDayLabelSpacing),
    ) {
        val indicatorModifier =
            Modifier
                .size(AppComponentSize.weekDayIndicator)
                .clip(CircleShape)

        when {
            weekDay.isCompleted -> {
                Box(
                    modifier = indicatorModifier.background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check_18),
                        contentDescription = null,
                        modifier = Modifier.size(AppIconSize.small),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            weekDay.isToday -> {
                Box(
                    modifier =
                        indicatorModifier
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                border =
                                    BorderStroke(
                                        width = todayIndicatorBorderWidth,
                                        color = MaterialTheme.colorScheme.primary,
                                    ),
                                shape = CircleShape,
                            ),
                )
            }

            else -> {
                Box(
                    modifier =
                        indicatorModifier
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                border =
                                    BorderStroke(
                                        width = upcomingIndicatorBorderWidth,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                    ),
                                shape = CircleShape,
                            ),
                )
            }
        }

        if (weekDay.isToday) {
            Text(
                text = dayLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        } else {
            Text(
                text = dayLabel,
                style = MaterialTheme.extraTypography.captionRegular,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFBFA, widthDp = 390)
@Composable
private fun HomeWeekCleaningCardPreview() {
    EcoGuardTheme {
        HomeWeekCleaningCard(
            weekCleaning = HomePreviewFixtures.weekCleaning(isTodayCompleted = false),
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
