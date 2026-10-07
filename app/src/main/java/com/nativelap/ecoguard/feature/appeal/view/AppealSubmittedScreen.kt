package com.nativelap.ecoguard.feature.appeal.view

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
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.CenteredIconStyle
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.component.InfoTableBox
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val submittedMessageBottomPadding = 32.dp

/** 09-2 이의신청 완료. 대상 인증과 보낸 시각을 보여준다. */
@Composable
fun AppealSubmittedScreen(
    targetVerificationDateTime: String,
    sentDateTime: String,
    onEvent: (AppealScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BottomCtaBar {
                EcoSecondaryButton(
                    text = stringResource(R.string.action_home),
                    onClick = { onEvent(AppealScreenEvent.HomeClick) },
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
                    title = stringResource(R.string.appeal_submitted),
                    description = stringResource(R.string.appeal_submitted_description),
                    iconStyle = CenteredIconStyle.HERO,
                    modifier = Modifier.padding(bottom = submittedMessageBottomPadding),
                )

                InfoTableBox {
                    InfoRow(
                        label = stringResource(R.string.appeal_target_verification),
                        value = targetVerificationDateTime,
                    )

                    InfoRow(
                        label = stringResource(R.string.appeal_sent_at),
                        value = sentDateTime,
                    )
                }
            }
        }
    }
}

@Preview(name = "Appeal submitted", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppealSubmittedScreenPreview() {
    EcoGuardTheme {
        AppealSubmittedScreen(
            targetVerificationDateTime = "9월 22일(화) 08:04",
            sentDateTime = "9월 29일(화) 12:20",
            onEvent = {},
        )
    }
}
