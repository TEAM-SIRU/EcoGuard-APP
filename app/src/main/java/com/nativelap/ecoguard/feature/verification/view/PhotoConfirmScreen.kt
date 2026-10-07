package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationStep
import com.nativelap.ecoguard.ui.component.AdaptiveButtonRow
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.component.InfoTableBox
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.PhotoPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val capturedPhotoHeight = 256.dp

/** 06-3 촬영한 사진 확인. 다시 찍거나 보낼 수 있다. */
@Composable
fun PhotoConfirmScreen(
    cleaningStartTime: String,
    cleaningEndTime: String,
    areaName: String,
    capturedTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(
                onBackClick = { onEvent(VerificationScreenEvent.BackClick) },
                trailingText = stringResource(
                    R.string.format_step,
                    VerificationStep.CONFIRM.stepNumber,
                    VerificationStep.totalStepCount,
                ),
            )
        },
        bottomBar = {
            BottomCtaBar {
                AdaptiveButtonRow(
                    firstButton = { itemModifier ->
                        EcoSecondaryButton(
                            text = stringResource(R.string.photo_retake),
                            onClick = { onEvent(VerificationScreenEvent.RetakeClick) },
                            modifier = itemModifier,
                        )
                    },
                    secondButton = { itemModifier ->
                        EcoPrimaryButton(
                            text = stringResource(R.string.photo_send),
                            onClick = { onEvent(VerificationScreenEvent.SendClick) },
                            modifier = itemModifier,
                        )
                    },
                )
            }
        },
    ) { innerPadding ->
        Box(
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
                    .padding(horizontal = AppSpacing.screenHorizontal),
            ) {
                PageTitle(
                    title = stringResource(R.string.photo_submit_confirmation_title),
                    subtitle = stringResource(R.string.photo_submit_confirmation_description),
                    modifier = Modifier.padding(
                        top = AppSpacing.xs,
                        bottom = AppSpacing.lg,
                    ),
                )

                PhotoPlaceholder(
                    label = stringResource(R.string.photo_captured),
                    height = capturedPhotoHeight,
                    modifier = Modifier.padding(bottom = AppSpacing.xl),
                )

                InfoTableBox {
                    InfoRow(
                        label = stringResource(R.string.home_cleaning_time),
                        value = stringResource(R.string.format_time_range, cleaningStartTime, cleaningEndTime),
                    )

                    InfoRow(
                        label = stringResource(R.string.common_assigned_area),
                        value = areaName,
                    )

                    InfoRow(
                        label = stringResource(R.string.verification_captured_at),
                        value = stringResource(R.string.format_today, capturedTime),
                    )
                }
            }
        }
    }
}

@Preview(name = "Photo confirm", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Photo confirm · compact larger text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun PhotoConfirmScreenPreview() {
    EcoGuardTheme {
        PhotoConfirmScreen(
            cleaningStartTime = "08:00",
            cleaningEndTime = "08:10",
            areaName = "본관 2층 복도 A",
            capturedTime = "08:04",
            onEvent = {},
        )
    }
}
