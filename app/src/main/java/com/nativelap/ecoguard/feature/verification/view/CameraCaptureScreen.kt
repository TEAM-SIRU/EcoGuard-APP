package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationStep
import com.nativelap.ecoguard.ui.component.LightSystemBarIconsEffect
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val countdownBarHeight = 36.dp
private val guideFrameBorderWidth = 2.dp
private val guideFrameDashLength = 8.dp
private val guideFrameDashGap = 6.dp
private const val GUIDE_MESSAGE_OVERLAY_ALPHA = 0.8f
private val guideMessageHorizontalPadding = 14.dp
private val shutterSize = 72.dp
private val shutterBorderWidth = 4.dp
private val shutterInnerPadding = 6.dp
private val controlsHorizontalPadding = 40.dp
private val controlsTopPadding = 24.dp
private val controlsBottomPadding = 32.dp

/**
 * 06-2 촬영. 화면 전체가 어두운 카메라 배경이라 Scaffold 대신 Column이 inset을 직접 처리한다.
 * 뷰파인더 자리에 가이드 프레임과 안내만 그린다.
 * 실제 카메라 미리보기는 카메라 기능 작업(CameraX 도입 검토)에서 연결한다.
 */
@Composable
fun CameraCaptureScreen(
    areaName: String,
    timeRemaining: String,
    deadlineTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cameraColors = MaterialTheme.extraColors
    val shutterDescription = stringResource(R.string.cd_shutter)

    LightSystemBarIconsEffect()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cameraColors.cameraBackgroundColor)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppSpacing.sm,
                    end = AppSpacing.lg,
                    top = AppSpacing.xxs,
                    bottom = AppSpacing.sm,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onEvent(VerificationScreenEvent.CloseClick) },
                modifier = Modifier.size(AppComponentSize.minTouchTarget),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_24),
                    contentDescription = stringResource(R.string.cd_close),
                    modifier = Modifier.size(AppIconSize.standard),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = areaName,
                    modifier = Modifier.semantics { heading() },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(
                        R.string.camera_step_capture,
                        VerificationStep.CAPTURE.stepNumber,
                        VerificationStep.totalStepCount,
                    ),
                    style = MaterialTheme.extraTypography.captionRegular,
                    color = cameraColors.cameraSecondaryTextColor,
                )
            }

            Spacer(modifier = Modifier.size(AppComponentSize.minTouchTarget))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = countdownBarHeight)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.camera_time_remaining, timeRemaining, deadlineTime),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(cameraColors.cameraSurfaceColor)
                .padding(
                    horizontal = AppSpacing.screenHorizontal,
                    vertical = AppSpacing.lg,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .dashedGuideFrame()
                    .padding(AppSpacing.lg),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = stringResource(R.string.camera_frame_guide),
                    modifier = Modifier
                        .clip(RoundedCornerShape(AppRadius.pill))
                        .background(cameraColors.cameraOverlayColor.copy(alpha = GUIDE_MESSAGE_OVERLAY_ALPHA))
                        .padding(
                            horizontal = guideMessageHorizontalPadding,
                            vertical = AppSpacing.xs,
                        ),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(
                    start = controlsHorizontalPadding,
                    end = controlsHorizontalPadding,
                    top = controlsTopPadding,
                    bottom = controlsBottomPadding,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(AppComponentSize.minTouchTarget))

            Surface(
                onClick = { onEvent(VerificationScreenEvent.ShutterClick) },
                modifier = Modifier
                    .size(shutterSize)
                    .semantics { contentDescription = shutterDescription },
                shape = CircleShape,
                color = cameraColors.cameraBackgroundColor,
                border = BorderStroke(
                    width = shutterBorderWidth,
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .padding(shutterInnerPadding)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary),
                )
            }

            IconButton(
                onClick = { onEvent(VerificationScreenEvent.FlipCameraClick) },
                modifier = Modifier
                    .size(AppComponentSize.minTouchTarget)
                    .clip(CircleShape)
                    .background(cameraColors.cameraSurfaceColor),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flip),
                    contentDescription = stringResource(R.string.cd_flip_camera),
                    modifier = Modifier.size(AppIconSize.standard),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

// Figma 가이드 프레임의 흰색 점선 테두리.
@Composable
private fun Modifier.dashedGuideFrame(): Modifier {
    val frameColor = MaterialTheme.colorScheme.onPrimary

    return drawBehind {
        val strokeWidthPx = guideFrameBorderWidth.toPx()
        val cornerRadiusPx = AppRadius.card.toPx()

        drawRoundRect(
            color = frameColor,
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
            size = Size(
                width = size.width - strokeWidthPx,
                height = size.height - strokeWidthPx,
            ),
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            style = Stroke(
                width = strokeWidthPx,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(
                        guideFrameDashLength.toPx(),
                        guideFrameDashGap.toPx(),
                    ),
                ),
            ),
        )
    }
}

@Preview(name = "Camera capture", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CameraCaptureScreenPreview() {
    EcoGuardTheme {
        CameraCaptureScreen(
            areaName = "본관 2층 복도 A",
            timeRemaining = "05:32",
            deadlineTime = "08:10",
            onEvent = {},
        )
    }
}
