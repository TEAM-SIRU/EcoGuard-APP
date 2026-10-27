package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentViewModel

@Composable
fun RecruitmentRoute(
    onEvent: (RecruitmentScreenEvent) -> Unit = {},
    onOpenApplication: (recruitmentId: Long, grade: Int, classNo: Int) -> Unit = { _, _, _ -> },
    viewModel: RecruitmentViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<RecruitmentViewModel>()
    val uiState by screenViewModel.uiState.collectAsState()

    LaunchedEffect(screenViewModel) {
        screenViewModel.loadRecruitment()
    }

    when (val currentUiState = uiState) {
        RecruitmentUiState.Loading -> {
            RecruitmentLoadingScreen()
        }

        RecruitmentUiState.LoadFailed -> {
            RecruitmentLoadFailedScreen(
                onEvent = { event ->
                    if (event == RecruitmentScreenEvent.RetryClick) {
                        screenViewModel.loadRecruitment()
                    } else {
                        onEvent(event)
                    }
                },
            )
        }

        is RecruitmentUiState.Content -> {
            RecruitmentScreen(
                uiState = currentUiState,
                onEvent = { event ->
                    if (event == RecruitmentScreenEvent.ApplyClick) {
                        onOpenApplication(
                            currentUiState.recruitmentId,
                            currentUiState.grade,
                            currentUiState.classNumber,
                        )
                    } else {
                        onEvent(event)
                    }
                },
            )
        }
    }
}
