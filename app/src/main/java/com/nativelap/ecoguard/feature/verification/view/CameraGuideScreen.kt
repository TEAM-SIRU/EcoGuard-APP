package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationStep
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.TwoLineTextRow
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val guideIllustrationHeight = 180.dp
private val guideIllustrationIconSize = 40.dp

/** 06-1 촬영 안내. 담당 구역과 촬영 요령을 보여주고 촬영 화면으로 이동한다. */
@Composable
fun CameraGuideScreen(
    areaName: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(
                onBackClick = { onEvent(VerificationScreenEvent.BackClick) },
                trailingText =
                    stringResource(
                        R.string.format_step,
                        VerificationStep.GUIDE.stepNumber,
                        VerificationStep.totalStepCount,
                    ),
            )
        },
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.camera_take_photo),
                    onClick = { onEvent(VerificationScreenEvent.TakePhotoClick) },
                    leadingIconRes = R.drawable.ic_cam,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = AppComponentSize.contentMaxWidth)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                PageTitle(
                    title = stringResource(R.string.camera_how_to_take_photo),
                    subtitle = stringResource(R.string.camera_one_photo_for_today),
                    modifier =
                        Modifier.padding(
                            start = AppSpacing.screenHorizontal,
                            end = AppSpacing.screenHorizontal,
                            top = AppSpacing.xs,
                            bottom = AppSpacing.lg,
                        ),
                )

                Column(
                    modifier =
                        Modifier
                            .padding(
                                start = AppSpacing.screenHorizontal,
                                end = AppSpacing.screenHorizontal,
                                bottom = AppSpacing.xs,
                            ).fillMaxWidth()
                            .height(guideIllustrationHeight)
                            .clip(RoundedCornerShape(AppRadius.card))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.spacedBy(
                            space = AppSpacing.sm,
                            alignment = Alignment.CenterVertically,
                        ),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_frame),
                        contentDescription = null,
                        modifier = Modifier.size(guideIllustrationIconSize),
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = areaName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                TwoLineTextRow(
                    title = stringResource(R.string.camera_show_entire_area),
                    subtitle = stringResource(R.string.camera_capture_end_of_corridor),
                    leadingIconRes = R.drawable.ic_frame_22,
                )

                TwoLineTextRow(
                    title = stringResource(R.string.camera_take_clear_photo),
                    subtitle = stringResource(R.string.camera_dark_photo_may_be_rejected),
                    leadingIconRes = R.drawable.ic_sun,
                )

                TwoLineTextRow(
                    title = stringResource(R.string.camera_gallery_not_available),
                    subtitle = stringResource(R.string.camera_one_submission_per_day),
                    leadingIconRes = R.drawable.ic_ban,
                )
            }
        }
    }
}

@Preview(name = "Camera guide", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(
    name = "Camera guide · compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Composable
private fun CameraGuideScreenPreview() {
    EcoGuardTheme {
        CameraGuideScreen(
            areaName = "본관 2층 복도 A",
            onEvent = {},
        )
    }
}
