package com.nativelap.ecoguard.feature.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.feature.home.viewmodel.HomeViewModel

@Composable
fun HomeRoute(
    onEvent: (HomeScreenEvent) -> Unit,
    viewModel: HomeViewModel? = null,
) {
    val homeViewModel = viewModel ?: rememberActivityViewModel<HomeViewModel>()
    val uiState = homeViewModel.uiState.collectAsState().value
    val handleEvent: (HomeScreenEvent) -> Unit = { event ->
        when (event) {
            HomeScreenEvent.RetryClick -> homeViewModel.loadHome()
            HomeScreenEvent.NoticeCloseClick -> homeViewModel.dismissNotice()
            else -> onEvent(event)
        }
    }
    when (uiState) {
        HomeUiState.Loading -> {
            HomeLoadingScreen(onEvent = handleEvent)
        }

        HomeUiState.LoadFailed -> {
            HomeLoadFailedScreen(onEvent = handleEvent)
        }

        is HomeUiState.RemovedFromActivity -> {
            HomeActivityRemovedScreen(
                removalReason = uiState.removalReason,
                onEvent = handleEvent,
            )
        }

        is HomeUiState.Content -> {
            HomeScreen(
                uiState = uiState,
                onEvent = handleEvent,
            )
        }
    }
}
