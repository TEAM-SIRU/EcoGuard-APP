package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationResultUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationViewModel
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent

@Composable
fun ApplicationRoute(
    recruitmentId: Long,
    uiState: ApplicationUiState,
    onEvent: (RecruitmentScreenEvent) -> Unit = {},
    onSubmitted: (ApplicationResultUiState.Completed) -> Unit = {},
    viewModel: ApplicationViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<ApplicationViewModel>()
    val applicationUiState by screenViewModel.uiState.collectAsState()
    val submissionResult by screenViewModel.submissionResult.collectAsState()

    LaunchedEffect(screenViewModel, recruitmentId) {
        screenViewModel.initialize(recruitmentId, uiState)
    }

    LaunchedEffect(screenViewModel, submissionResult) {
        submissionResult?.let { completedState ->
            screenViewModel.consumeSubmissionResult()
            onSubmitted(completedState)
        }
    }

    val applicationState = applicationUiState ?: uiState
    ApplicationScreen(
        uiState = applicationState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                is RecruitmentScreenEvent.MotivationChange -> {
                    screenViewModel.updateMotivation(screenEvent.motivation)
                }

                RecruitmentScreenEvent.ApplyClick -> {
                    screenViewModel.submitApplication()
                }

                else -> {
                    onEvent(screenEvent)
                }
            }
        },
    )
}
