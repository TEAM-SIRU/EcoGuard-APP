package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.CenteredIconStyle
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.component.InfoTableBox
import com.nativelap.ecoguard.ui.component.PhotoPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val submittedPhotoHeight = 200.dp
private val submittedMessageBottomPadding = 32.dp

/** 06-4 제출 완료. AI 검수가 진행 중임을 알리고 제출 정보를 보여준다. */
@Composable
fun SubmissionCompletedScreen(
    areaName: String,
    submittedTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.action_home),
                    onClick = { onEvent(VerificationScreenEvent.HomeClick) },
                )
            }
        },
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .padding(
                        horizontal = AppSpacing.screenHorizontal,
                        vertical = AppSpacing.xl,
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                CenteredIconMessage(
                    iconRes = R.drawable.ic_clock_64,
                    title = stringResource(R.string.photo_submitted),
                    description = stringResource(R.string.photo_review_pending_description),
                    iconStyle = CenteredIconStyle.HERO,
                    modifier = Modifier.padding(bottom = submittedMessageBottomPadding),
                )

                PhotoPlaceholder(
                    label = stringResource(R.string.photo_submitted_label),
                    height = submittedPhotoHeight,
                    modifier = Modifier.padding(bottom = AppSpacing.xl),
                )

                InfoTableBox {
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
        }
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
