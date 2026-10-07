package com.nativelap.ecoguard.feature.recruitment.view

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
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationResultUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.CenteredIconStyle
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun ApplicationResultScreen(
    uiState: ApplicationResultUiState,
    onEvent: (RecruitmentScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BottomCtaBar {
                when (uiState) {
                    is ApplicationResultUiState.Completed -> EcoPrimaryButton(
                        text = stringResource(R.string.action_home),
                        onClick = { onEvent(RecruitmentScreenEvent.HomeClick) },
                    )
                    ApplicationResultUiState.FilledWhileApplying -> EcoSecondaryButton(
                        text = stringResource(R.string.action_home),
                        onClick = { onEvent(RecruitmentScreenEvent.HomeClick) },
                    )
                }
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
                when (uiState) {
                    is ApplicationResultUiState.Completed -> CenteredIconMessage(
                        iconRes = R.drawable.ic_check_64,
                        title = stringResource(R.string.home_became_guardian),
                        description = stringResource(
                            R.string.recruitment_application_position,
                            uiState.applicationOrder,
                            uiState.appliedDateTime,
                        ),
                        iconStyle = CenteredIconStyle.HERO,
                    )
                    ApplicationResultUiState.FilledWhileApplying -> CenteredIconMessage(
                        iconRes = R.drawable.ic_x_64,
                        title = stringResource(R.string.recruitment_application_failed),
                        description = stringResource(R.string.recruitment_filled_while_applying),
                        iconStyle = CenteredIconStyle.HERO,
                    )
                }
            }
        }
    }
}

@Preview(name = "Application result · completed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ApplicationResultScreenCompletedPreview() {
    EcoGuardTheme {
        ApplicationResultScreen(
            uiState = ApplicationResultUiState.Completed(
                applicationOrder = 4,
                appliedDateTime = "9월 1일(화) 12:34",
            ),
            onEvent = {},
        )
    }
}

@Preview(name = "Application result · filled", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ApplicationResultScreenFilledPreview() {
    EcoGuardTheme {
        ApplicationResultScreen(
            uiState = ApplicationResultUiState.FilledWhileApplying,
            onEvent = {},
        )
    }
}
