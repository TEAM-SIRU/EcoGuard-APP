package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityViewModel
import java.time.YearMonth

@Composable
fun ActivityRoute(
    onEvent: (ActivityScreenEvent) -> Unit,
    initialMonthPickerVisible: Boolean = false,
    useNavigationOverlays: Boolean = false,
    viewModel: ActivityViewModel? = null,
) {
    val activityViewModel = viewModel ?: rememberActivityViewModel<ActivityViewModel>()
    val uiState = activityViewModel.uiState.collectAsState().value
    var isMonthPickerVisible by rememberSaveable { mutableStateOf(initialMonthPickerVisible) }

    if (uiState == ActivityUiState.Loading) {
        ActivityLoadingScreen(onEvent = onEvent)
        return
    }

    ActivityScreen(
        uiState = uiState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                ActivityScreenEvent.MonthSelectorClick -> {
                    if (useNavigationOverlays) onEvent(screenEvent) else isMonthPickerVisible = true
                }

                is ActivityScreenEvent.MonthApply -> {
                    activityViewModel.loadActivity(screenEvent.year, screenEvent.month)
                    if (useNavigationOverlays) onEvent(screenEvent)
                }

                ActivityScreenEvent.RetryClick -> {
                    activityViewModel.loadActivity()
                }

                else -> {
                    onEvent(screenEvent)
                }
            }
        },
    )

    if (isMonthPickerVisible && uiState is ActivityUiState.Content) {
        MonthPickerDialog(
            initialYear = uiState.year,
            initialMonth = uiState.month,
            latestYear = YearMonth.now().year,
            latestMonth = YearMonth.now().monthValue,
            onDismissRequest = { isMonthPickerVisible = false },
            onApply = { selectedYear, selectedMonth ->
                isMonthPickerVisible = false
                activityViewModel.loadActivity(selectedYear, selectedMonth)
            },
        )
    }
}
