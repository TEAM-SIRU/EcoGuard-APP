package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationDetailUiState
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationDetailViewModel
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun VerificationDetailRoute(
    verificationId: Long,
    onEvent: (VerificationResultScreenEvent) -> Unit,
    viewModel: VerificationDetailViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<VerificationDetailViewModel>()
    val uiState by screenViewModel.uiState.collectAsState()

    LaunchedEffect(screenViewModel, verificationId) {
        screenViewModel.load(verificationId)
    }

    when (val state = uiState) {
        VerificationDetailUiState.Loading -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) }) },
            ) { padding ->
                CenteredScrollContent(innerPadding = padding) {
                    CircularProgressIndicator()
                }
            }
        }

        VerificationDetailUiState.Failed -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) }) },
                bottomBar = {
                    BottomCtaBar {
                        EcoPrimaryButton(
                            text = stringResource(R.string.action_retry),
                            onClick = { onEvent(VerificationResultScreenEvent.RetryClick) },
                        )
                    }
                },
            ) { padding ->
                CenteredScrollContent(innerPadding = padding) {
                    Text(stringResource(R.string.activity_load_failed))
                }
            }
        }

        is VerificationDetailUiState.Content -> {
            VerificationDetailScreen(
                verificationDate = state.verificationDate,
                statusDescription = state.rejectionReason ?: stringResource(state.statusDescriptionRes),
                status = state.status,
                areaName = state.areaName,
                submittedDateTime = state.submittedDateTime,
                onEvent = { event ->
                    if (event == VerificationResultScreenEvent.RetryClick) {
                        screenViewModel.load(verificationId)
                    } else {
                        onEvent(event)
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun VerificationDetailRouteLoadingPreview() {
    EcoGuardTheme {
        Scaffold { padding ->
            CenteredScrollContent(innerPadding = padding) { Text(stringResource(R.string.common_loading)) }
        }
    }
}
