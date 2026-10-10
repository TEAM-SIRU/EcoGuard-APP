package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultUiState
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.component.RejectionResultContent
import com.nativelap.ecoguard.ui.component.StatusMessage
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 08 인증 결과(승인·선생님 확인 중·반려·조회 실패). */
@Composable
fun VerificationResultScreen(
    uiState: VerificationResultUiState,
    onEvent: (VerificationResultScreenEvent) -> Unit,
) {
    when (uiState) {
        VerificationResultUiState.LoadFailed -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) })
                },
                bottomBar = {
                    BottomCtaBar(minHeight = AppComponentSize.stateBottomCtaMinHeight) {
                        EcoPrimaryButton(
                            text = stringResource(R.string.result_check_again),
                            onClick = { onEvent(VerificationResultScreenEvent.RetryClick) },
                        )

                        EcoSecondaryButton(
                            text = stringResource(R.string.action_home),
                            onClick = { onEvent(VerificationResultScreenEvent.HomeClick) },
                        )
                    }
                },
            ) { innerPadding ->
                CenteredScrollContent(innerPadding = innerPadding) {
                    StatusMessage(
                        title = stringResource(R.string.result_load_failed),
                        description = stringResource(R.string.result_load_failed_description),
                    )
                }
            }
        }

        is VerificationResultUiState.Approved -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    BottomCtaBar {
                        EcoPrimaryButton(
                            text = stringResource(R.string.action_confirm),
                            onClick = { onEvent(VerificationResultScreenEvent.ConfirmClick) },
                        )
                    }
                },
            ) { innerPadding ->
                CenteredScrollContent(innerPadding = innerPadding) {
                    VerificationSummaryContent(
                        resultIconRes = R.drawable.ic_check_64,
                        title = stringResource(R.string.verification_completed),
                        description =
                            stringResource(
                                R.string.verification_completed_description,
                                uiState.earnedMinutes,
                            ),
                    ) {
                        InfoRow(
                            label = stringResource(R.string.common_assigned_area),
                            value = uiState.areaName,
                        )

                        InfoRow(
                            label = stringResource(R.string.verification_submitted_at),
                            value = stringResource(R.string.format_today, uiState.submittedTime),
                        )

                        InfoRow(
                            label = stringResource(R.string.verification_earned_time),
                            value = stringResource(R.string.format_bonus_minutes, uiState.earnedMinutes),
                            valueColor = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }

        is VerificationResultUiState.TeacherReviewing -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    BottomCtaBar {
                        EcoPrimaryButton(
                            text = stringResource(R.string.action_confirm),
                            onClick = { onEvent(VerificationResultScreenEvent.ConfirmClick) },
                        )
                    }
                },
            ) { innerPadding ->
                CenteredScrollContent(innerPadding = innerPadding) {
                    VerificationSummaryContent(
                        resultIconRes = R.drawable.ic_review_64,
                        title = stringResource(R.string.verification_teacher_review_title),
                        description = stringResource(R.string.verification_teacher_review_description),
                    ) {
                        InfoRow(
                            label = stringResource(R.string.common_assigned_area),
                            value = uiState.areaName,
                        )

                        InfoRow(
                            label = stringResource(R.string.verification_submitted_at),
                            value = stringResource(R.string.format_today, uiState.submittedTime),
                        )

                        InfoRow(
                            label = stringResource(R.string.photo_status),
                            value = stringResource(R.string.verification_teacher_review_pending),
                            valueColor = MaterialTheme.extraColors.warningTextColor,
                        )
                    }
                }
            }
        }

        is VerificationResultUiState.Rejected -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) })
                },
            ) { innerPadding ->
                RejectionResultContent(
                    innerPadding = innerPadding,
                    title = stringResource(R.string.common_verification_rejected),
                    summary =
                        stringResource(
                            R.string.result_submitted_summary,
                            uiState.areaName,
                            uiState.submittedTime,
                        ),
                    feedbackLabel = stringResource(R.string.verification_ai_review_result),
                    feedbackHeadline = uiState.rejectionReason,
                    feedbackBody = uiState.retakeGuide,
                    primaryActionText = stringResource(R.string.home_appeal),
                    actionCaption = stringResource(R.string.verification_request_teacher_review),
                    onPrimaryActionClick = { onEvent(VerificationResultScreenEvent.AppealClick) },
                    onHomeClick = { onEvent(VerificationResultScreenEvent.HomeClick) },
                )
            }
        }
    }
}

@Preview(name = "Result · approved", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationResultApprovedPreview() {
    EcoGuardTheme {
        VerificationResultScreen(
            uiState =
                VerificationResultUiState.Approved(
                    areaName = "본관 2층 복도 A",
                    submittedTime = "08:04",
                    earnedMinutes = 10,
                ),
            onEvent = {},
        )
    }
}

@Preview(name = "Result · rejected", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationResultRejectedPreview() {
    EcoGuardTheme {
        VerificationResultScreen(
            uiState =
                VerificationResultUiState.Rejected(
                    areaName = "본관 2층 복도 A",
                    submittedTime = "08:04",
                    rejectionReason = "사진에 청소 구역이 잘 보이지 않아요",
                    retakeGuide = "복도 끝까지 보이도록 조금 뒤에서 찍으면 돼요",
                ),
            onEvent = {},
        )
    }
}

@Preview(name = "Result · teacher reviewing", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationResultTeacherReviewingPreview() {
    EcoGuardTheme {
        VerificationResultScreen(
            uiState =
                VerificationResultUiState.TeacherReviewing(
                    areaName = "본관 2층 복도 A",
                    submittedTime = "08:04",
                ),
            onEvent = {},
        )
    }
}

@Preview(name = "Verification result · failed · 823:4317", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationResultFailedPreview() {
    EcoGuardTheme { VerificationResultScreen(uiState = VerificationResultUiState.LoadFailed, onEvent = {}) }
}
