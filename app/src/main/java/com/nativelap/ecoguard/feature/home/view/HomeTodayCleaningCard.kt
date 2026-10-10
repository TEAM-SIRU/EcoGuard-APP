package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningUiModel
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val todayCardTopPadding = 18.dp
private val todayTitleTopPadding = 8.dp
private val reasonBoxHorizontalPadding = 14.dp
private val reasonTextSpacing = 2.dp
private val reasonBorderWidth = 1.dp

/** 오늘의 청소 카드. 인증 상태에 따라 제목·정보·안내·버튼이 바뀐다. */
@Composable
fun HomeTodayCleaningCard(
    todayCleaning: TodayCleaningUiModel,
    onVerificationClick: () -> Unit,
    onSubmittedPhotoClick: () -> Unit,
    onActivityRecordClick: () -> Unit,
    onAppealClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cleaningStatus = todayCleaning.status

    EcoCard(
        modifier = modifier,
        contentPadding =
            PaddingValues(
                start = AppSpacing.md,
                end = AppSpacing.md,
                top = todayCardTopPadding,
                bottom = AppSpacing.md,
            ),
    ) {
        Text(
            text = stringResource(R.string.home_today_cleaning),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = todayCleaningTitle(cleaningStatus),
            modifier =
                Modifier
                    .padding(
                        top = todayTitleTopPadding,
                        bottom = AppSpacing.md,
                    ).semantics { heading() },
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            InfoRow(
                label = stringResource(R.string.home_cleaning_time),
                value =
                    stringResource(
                        R.string.format_time_range,
                        todayCleaning.cleaningStartTime,
                        todayCleaning.cleaningEndTime,
                    ),
            )

            InfoRow(
                label = stringResource(R.string.common_assigned_area),
                value = todayCleaning.assignedAreaName,
            )

            TodayCleaningExtraInfoRow(cleaningStatus = cleaningStatus)
        }

        TodayCleaningNotice(cleaningStatus = cleaningStatus)

        TodayCleaningAction(
            cleaningStatus = cleaningStatus,
            onVerificationClick = onVerificationClick,
            onSubmittedPhotoClick = onSubmittedPhotoClick,
            onActivityRecordClick = onActivityRecordClick,
            onAppealClick = onAppealClick,
            modifier = Modifier.padding(top = AppSpacing.lg),
        )
    }
}

@Composable
private fun todayCleaningTitle(cleaningStatus: TodayCleaningStatus): String =
    when (cleaningStatus) {
        is TodayCleaningStatus.NotSubmitted -> {
            stringResource(R.string.home_not_submitted)
        }

        is TodayCleaningStatus.OutsideVerificationTime -> {
            stringResource(
                R.string.home_verification_available_from,
                cleaningStatus.availableFromTime,
            )
        }

        is TodayCleaningStatus.AiReviewing -> {
            stringResource(R.string.home_ai_reviewing)
        }

        is TodayCleaningStatus.TeacherReviewing -> {
            stringResource(R.string.home_teacher_reviewing)
        }

        is TodayCleaningStatus.Approved -> {
            stringResource(R.string.home_cleaning_completed)
        }

        is TodayCleaningStatus.Rejected -> {
            stringResource(R.string.common_verification_rejected)
        }
    }

@Composable
private fun TodayCleaningExtraInfoRow(cleaningStatus: TodayCleaningStatus) {
    when (cleaningStatus) {
        is TodayCleaningStatus.NotSubmitted -> {
            InfoRow(
                label = stringResource(R.string.home_verification_deadline),
                value = stringResource(R.string.home_deadline_remaining, cleaningStatus.timeUntilDeadline),
                valueColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        is TodayCleaningStatus.AiReviewing -> {
            InfoRow(
                label = stringResource(R.string.home_submit),
                value = stringResource(R.string.format_today, cleaningStatus.submittedTime),
            )
        }

        is TodayCleaningStatus.TeacherReviewing -> {
            InfoRow(
                label = stringResource(R.string.home_submit),
                value = stringResource(R.string.format_today, cleaningStatus.submittedTime),
            )
        }

        is TodayCleaningStatus.Approved -> {
            InfoRow(
                label = stringResource(R.string.verification_earned_time),
                value = stringResource(R.string.format_bonus_minutes, cleaningStatus.earnedMinutes),
                valueColor = MaterialTheme.colorScheme.primary,
            )
        }

        is TodayCleaningStatus.OutsideVerificationTime,
        is TodayCleaningStatus.Rejected,
        -> {
            Unit
        }
    }
}

@Composable
private fun TodayCleaningNotice(cleaningStatus: TodayCleaningStatus) {
    when (cleaningStatus) {
        is TodayCleaningStatus.NotSubmitted -> {
            Unit
        }

        is TodayCleaningStatus.TeacherReviewing -> {
            Text(
                text = stringResource(R.string.home_ai_referred_to_teacher),
                modifier =
                    Modifier
                        .padding(top = AppSpacing.sm)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppRadius.tile))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(
                            horizontal = reasonBoxHorizontalPadding,
                            vertical = AppSpacing.sm,
                        ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        is TodayCleaningStatus.Rejected -> {
            RejectionReasonBox(
                rejectionReason = cleaningStatus.rejectionReason,
                modifier = Modifier.padding(top = AppSpacing.sm),
            )
        }

        is TodayCleaningStatus.OutsideVerificationTime,
        is TodayCleaningStatus.AiReviewing,
        is TodayCleaningStatus.Approved,
        -> {
            Unit
        }
    }
}

@Composable
private fun RejectionReasonBox(
    rejectionReason: String,
    modifier: Modifier = Modifier,
) {
    val reasonShape = RoundedCornerShape(AppRadius.tile)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(reasonShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    border =
                        BorderStroke(
                            width = reasonBorderWidth,
                            color = MaterialTheme.colorScheme.outlineVariant,
                        ),
                    shape = reasonShape,
                ).padding(
                    horizontal = reasonBoxHorizontalPadding,
                    vertical = AppSpacing.sm,
                ),
        verticalArrangement = Arrangement.spacedBy(reasonTextSpacing),
    ) {
        Text(
            text = stringResource(R.string.appeal_rejection_reason),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
        )

        Text(
            text = rejectionReason,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun TodayCleaningAction(
    cleaningStatus: TodayCleaningStatus,
    onVerificationClick: () -> Unit,
    onSubmittedPhotoClick: () -> Unit,
    onActivityRecordClick: () -> Unit,
    onAppealClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (cleaningStatus) {
        is TodayCleaningStatus.NotSubmitted -> {
            EcoPrimaryButton(
                text = stringResource(R.string.home_cleaning_submit),
                onClick = onVerificationClick,
                modifier = modifier,
                leadingIconRes = R.drawable.ic_cam,
            )
        }

        is TodayCleaningStatus.OutsideVerificationTime -> {
            EcoSecondaryButton(
                text = stringResource(R.string.home_outside_verification_time),
                onClick = {},
                modifier = modifier,
                leadingIconRes = R.drawable.ic_clock_20,
                enabled = false,
            )
        }

        is TodayCleaningStatus.AiReviewing,
        is TodayCleaningStatus.TeacherReviewing,
        -> {
            EcoSecondaryButton(
                text = stringResource(R.string.home_view_submitted_photo),
                onClick = onSubmittedPhotoClick,
                modifier = modifier,
            )
        }

        is TodayCleaningStatus.Approved -> {
            EcoSecondaryButton(
                text = stringResource(R.string.home_view_activity_record),
                onClick = onActivityRecordClick,
                modifier = modifier,
            )
        }

        is TodayCleaningStatus.Rejected -> {
            EcoPrimaryButton(
                text = stringResource(R.string.home_appeal),
                onClick = onAppealClick,
                modifier = modifier,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFBFA, widthDp = 390)
@Composable
private fun HomeTodayCleaningCardPreview() {
    EcoGuardTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            HomePreviewFixtures.todayStatuses.forEach { cleaningStatus ->
                HomeTodayCleaningCard(
                    todayCleaning = HomePreviewFixtures.todayCleaning(cleaningStatus),
                    onVerificationClick = {},
                    onSubmittedPhotoClick = {},
                    onActivityRecordClick = {},
                    onAppealClick = {},
                )
            }
        }
    }
}
