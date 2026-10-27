package com.nativelap.ecoguard.feature.area.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.feature.area.viewmodel.AreaViewModel

@Composable
fun AreaRoute(
    onEvent: (AreaScreenEvent) -> Unit,
    viewModel: AreaViewModel? = null,
) {
    val areaViewModel = viewModel ?: rememberActivityViewModel<AreaViewModel>()
    val uiState = areaViewModel.uiState.collectAsState().value

    when (uiState) {
        AreaUiState.Loading -> {
            AreaLoadingScreen(onEvent = onEvent)
        }

        else -> {
            AreaScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is AreaScreenEvent.FloorSelect -> areaViewModel.selectFloor(event.floor)
                        AreaScreenEvent.RetryClick -> areaViewModel.loadAssignment()
                        else -> onEvent(event)
                    }
                },
            )
        }
    }
}
