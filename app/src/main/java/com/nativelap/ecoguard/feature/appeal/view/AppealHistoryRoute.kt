package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryViewModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent

@Composable
fun AppealHistoryRoute(
    onEvent: (AppealScreenEvent) -> Unit = {},
    viewModel: AppealHistoryViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<AppealHistoryViewModel>()
    val uiState by screenViewModel.uiState.collectAsState()

    LaunchedEffect(screenViewModel) {
        screenViewModel.loadHistory()
    }

    AppealHistoryScreen(
        uiState = uiState,
        onEvent = { event ->
            if (event == AppealScreenEvent.RetryHistoryClick) {
                screenViewModel.loadHistory()
            } else {
                onEvent(event)
            }
        },
    )
}
