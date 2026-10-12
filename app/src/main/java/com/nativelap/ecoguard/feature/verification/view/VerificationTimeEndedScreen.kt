package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.StatusMessage
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 06-6 인증 시간 종료. 업로드 상태를 확인하거나 홈으로 돌아간다. */
@Composable
fun VerificationTimeEndedScreen(
    deadlineTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EcoBackTopBar(onBackClick = { onEvent(VerificationScreenEvent.BackClick) })
        },
        bottomBar = {
            BottomCtaBar(minHeight = AppComponentSize.stateBottomCtaMinHeight) {
                EcoPrimaryButton(
                    text = stringResource(R.string.upload_status_check),
                    onClick = { onEvent(VerificationScreenEvent.CheckUploadStatusClick) },
                )

                EcoSecondaryButton(
                    text = stringResource(R.string.action_home),
                    onClick = { onEvent(VerificationScreenEvent.HomeClick) },
                )
            }
        },
    ) { innerPadding ->
        CenteredScrollContent(innerPadding = innerPadding) {
            StatusMessage(
                iconRes = R.drawable.ic_clock_64,
                title = stringResource(R.string.verification_time_ended),
                description = stringResource(R.string.verification_time_ended_description, deadlineTime),
            )
        }
    }
}

@Preview(name = "Verification time ended", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationTimeEndedScreenPreview() {
    EcoGuardTheme {
        VerificationTimeEndedScreen(
            deadlineTime = "08:10",
            onEvent = {},
        )
    }
}
