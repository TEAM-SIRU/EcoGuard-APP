package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationBlockReason
import com.nativelap.ecoguard.ui.component.EcoInfoBottomSheet

/** 인증 시간 아님·오늘 이미 제출·카메라 권한 필요 안내 바텀시트. */
@Composable
fun VerificationBlockedSheet(
    blockReason: VerificationBlockReason,
    onPrimaryActionClick: () -> Unit,
    onSecondaryActionClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (blockReason) {
        is VerificationBlockReason.OutsideVerificationTime -> EcoInfoBottomSheet(
            iconRes = R.drawable.ic_clock_30,
            title = stringResource(R.string.home_outside_verification_time),
            description = stringResource(
                R.string.home_verification_time,
                blockReason.startTime,
                blockReason.endTime,
            ),
            primaryActionText = stringResource(R.string.action_confirm),
            onPrimaryActionClick = onPrimaryActionClick,
            onDismissRequest = onDismissRequest,
        )
        is VerificationBlockReason.AlreadySubmitted -> EcoInfoBottomSheet(
            iconRes = R.drawable.ic_check_30,
            iconTint = MaterialTheme.colorScheme.primary,
            title = stringResource(R.string.verification_already_submitted_title),
            description = stringResource(
                R.string.verification_already_submitted_description,
                blockReason.submittedTime,
            ),
            primaryActionText = stringResource(R.string.verification_view_submitted),
            onPrimaryActionClick = onPrimaryActionClick,
            secondaryActionText = stringResource(R.string.action_close),
            onSecondaryActionClick = onSecondaryActionClick,
            onDismissRequest = onDismissRequest,
        )
        VerificationBlockReason.CameraPermissionRequired -> EcoInfoBottomSheet(
            iconRes = R.drawable.ic_cam_30,
            title = stringResource(R.string.camera_permission_title),
            description = stringResource(R.string.camera_permission_description),
            primaryActionText = stringResource(R.string.camera_open_settings),
            onPrimaryActionClick = onPrimaryActionClick,
            secondaryActionText = stringResource(R.string.camera_later),
            onSecondaryActionClick = onSecondaryActionClick,
            onDismissRequest = onDismissRequest,
        )
    }
}
