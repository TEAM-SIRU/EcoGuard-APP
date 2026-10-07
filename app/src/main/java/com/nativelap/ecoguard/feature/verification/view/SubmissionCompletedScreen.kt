package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 06-4 제출 완료. AI 검수가 진행 중임을 알리고 제출 정보를 보여준다. */
@Composable
fun SubmissionCompletedScreen(
    areaName: String,
    submittedTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    VerificationSummaryLayout(
        resultIconRes = R.drawable.ic_clock_64,
        title = stringResource(R.string.photo_submitted),
        description = stringResource(R.string.photo_review_pending_description),
        actionText = stringResource(R.string.action_home),
        onActionClick = { onEvent(VerificationScreenEvent.HomeClick) },
        modifier = modifier,
    ) {
        InfoRow(
            label = stringResource(R.string.common_assigned_area),
            value = areaName,
        )

        InfoRow(
            label = stringResource(R.string.verification_submitted_at),
            value = stringResource(R.string.format_today, submittedTime),
        )

        InfoRow(
            label = stringResource(R.string.photo_status),
            value = stringResource(R.string.photo_ai_review_pending),
            valueColor = MaterialTheme.extraColors.warningTextColor,
        )
    }
}

@Preview(name = "Submission completed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SubmissionCompletedScreenPreview() {
    EcoGuardTheme {
        SubmissionCompletedScreen(
            areaName = "본관 2층 복도 A",
            submittedTime = "08:04",
            onEvent = {},
        )
    }
}
