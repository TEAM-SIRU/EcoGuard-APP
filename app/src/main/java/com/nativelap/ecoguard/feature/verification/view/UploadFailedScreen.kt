package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.StatusMessageLayout
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 06-5 업로드 실패. 같은 사진으로 다시 보내거나 홈으로 돌아간다. */
@Composable
fun UploadFailedScreen(
    deadlineTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusMessageLayout(
        title = stringResource(R.string.photo_upload_failed),
        description = stringResource(R.string.photo_upload_failed_description, deadlineTime),
        onBackClick = { onEvent(VerificationScreenEvent.BackClick) },
        modifier = modifier,
    ) {
        EcoPrimaryButton(
            text = stringResource(R.string.photo_resend_same),
            onClick = { onEvent(VerificationScreenEvent.ResendSamePhotoClick) },
        )

        EcoSecondaryButton(
            text = stringResource(R.string.action_home),
            onClick = { onEvent(VerificationScreenEvent.HomeClick) },
        )
    }
}

@Preview(name = "Upload failed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun UploadFailedScreenPreview() {
    EcoGuardTheme {
        UploadFailedScreen(
            deadlineTime = "08:10",
            onEvent = {},
        )
    }
}
