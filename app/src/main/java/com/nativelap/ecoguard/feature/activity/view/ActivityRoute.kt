package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. 월 선택 팝업 표시 여부만 Route가 관리한다.
@Composable
fun ActivityRoute(
    uiState: ActivityUiState = ActivityUiState.Loading,
    onEvent: (ActivityScreenEvent) -> Unit = {},
    initialMonthPickerVisible: Boolean = false,
) {
    var isMonthPickerVisible by rememberSaveable { mutableStateOf(initialMonthPickerVisible) }

    if (uiState == ActivityUiState.Loading) {
        ActivityLoadingScreen()
        return
    }

    ActivityScreen(
        uiState = uiState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                ActivityScreenEvent.MonthSelectorClick -> isMonthPickerVisible = true
                else -> onEvent(screenEvent)
            }
        },
    )

    if (isMonthPickerVisible && uiState is ActivityUiState.Content) {
        MonthPickerDialog(
            initialYear = uiState.year,
            initialMonth = uiState.month,
            onDismissRequest = { isMonthPickerVisible = false },
            onApply = { selectedYear, selectedMonth ->
                isMonthPickerVisible = false
                onEvent(ActivityScreenEvent.MonthApply(selectedYear, selectedMonth))
            },
        )
    }
}
