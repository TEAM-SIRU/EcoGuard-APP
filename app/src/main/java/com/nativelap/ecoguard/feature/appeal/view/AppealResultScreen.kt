package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealResultUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.RejectionResultContent
import com.nativelap.ecoguard.ui.component.StatusMessage
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 09-4 이의신청 결과(승인·반려)와 09 제출 실패. */
@Composable
fun AppealResultScreen(
    uiState: AppealResultUiState,
    onEvent: (AppealScreenEvent) -> Unit,
) {
    when (uiState) {
        is AppealResultUiState.Approved -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.surface,
                topBar = {
                    EcoBackTopBar(onBackClick = { onEvent(AppealScreenEvent.BackClick) })
                },
                bottomBar = {
                    BottomCtaBar {
                        EcoPrimaryButton(
                            text = stringResource(R.string.action_view_activity_record),
                            onClick = { onEvent(AppealScreenEvent.ActivityRecordClick) },
                        )

                        EcoSecondaryButton(
                            text = stringResource(R.string.action_home),
                            onClick = { onEvent(AppealScreenEvent.HomeClick) },
                        )
                    }
                },
            ) { innerPadding ->
                CenteredScrollContent(innerPadding = innerPadding) {
                    StatusMessage(
                        title = stringResource(R.string.appeal_approved),
                        highlightText = stringResource(R.string.format_bonus_minutes, uiState.earnedMinutes),
                        description =
                            stringResource(
                                R.string.appeal_approval_summary,
                                uiState.verificationDate,
                                uiState.attemptNumber,
                                uiState.earnedMinutes,
                            ),
                    )
                }
            }
        }

        is AppealResultUiState.Rejected -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.surface,
                topBar = {
                    EcoBackTopBar(onBackClick = { onEvent(AppealScreenEvent.BackClick) })
                },
            ) { innerPadding ->
                RejectionResultContent(
                    innerPadding = innerPadding,
                    title = stringResource(R.string.appeal_rejected),
                    summary =
                        stringResource(
                            R.string.appeal_attempt_summary,
                            uiState.verificationDate,
                            uiState.attemptNumber,
                        ),
                    feedbackLabel = stringResource(R.string.appeal_teacher_response),
                    feedbackHeadline = uiState.teacherResponseTitle,
                    feedbackBody = uiState.teacherResponseDetail,
                    primaryActionText = stringResource(R.string.appeal_resubmit),
                    actionCaption = stringResource(R.string.appeal_unlimited_attempts),
                    onPrimaryActionClick = { onEvent(AppealScreenEvent.ResubmitClick) },
                    onHomeClick = { onEvent(AppealScreenEvent.HomeClick) },
                )
            }
        }

        AppealResultUiState.SendFailed -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.surface,
                topBar = {
                    EcoBackTopBar(onBackClick = { onEvent(AppealScreenEvent.BackClick) })
                },
                bottomBar = {
                    BottomCtaBar {
                        EcoPrimaryButton(
                            text = stringResource(R.string.appeal_retry_send),
                            onClick = { onEvent(AppealScreenEvent.RetrySendClick) },
                        )

                        EcoSecondaryButton(
                            text = stringResource(R.string.appeal_edit_content),
                            onClick = { onEvent(AppealScreenEvent.EditContentClick) },
                        )
                    }
                },
            ) { innerPadding ->
                CenteredScrollContent(innerPadding = innerPadding) {
                    StatusMessage(
                        title = stringResource(R.string.appeal_send_failed),
                        description = stringResource(R.string.appeal_send_failed_description),
                    )
                }
            }
        }
    }
}

@Preview(name = "Appeal result · approved", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppealResultApprovedPreview() {
    EcoGuardTheme {
        AppealResultScreen(
            uiState = AppealPreviewFixtures.approvedResult,
            onEvent = {},
        )
    }
}

@Preview(name = "Appeal result · rejected", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppealResultRejectedPreview() {
    EcoGuardTheme {
        AppealResultScreen(
            uiState = AppealPreviewFixtures.rejectedResult,
            onEvent = {},
        )
    }
}

@Preview(name = "Appeal · send failed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppealSendFailedPreview() {
    EcoGuardTheme {
        AppealResultScreen(
            uiState = AppealResultUiState.SendFailed,
            onEvent = {},
        )
    }
}
