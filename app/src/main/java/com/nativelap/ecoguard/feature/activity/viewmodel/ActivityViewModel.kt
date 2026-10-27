package com.nativelap.ecoguard.feature.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.activity.module.domain.usecase.GetMyActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel
    @Inject
    constructor(
        private val getMyActivity: GetMyActivityUseCase,
    ) : ViewModel() {
        private val currentMonth = YearMonth.now()
        private val _uiState = MutableStateFlow<ActivityUiState>(ActivityUiState.Loading)
        val uiState = _uiState.asStateFlow()

        private var selectedMonth = currentMonth
        private var activityLoadJob: Job? = null

        init {
            loadActivity()
        }

        fun loadActivity(
            year: Int = selectedMonth.year,
            month: Int = selectedMonth.monthValue,
        ) {
            val requestedMonth = runCatching { YearMonth.of(year, month) }.getOrNull() ?: return
            if (requestedMonth.isAfter(YearMonth.now())) return
            selectedMonth = requestedMonth
            _uiState.value = ActivityUiState.Loading
            activityLoadJob?.cancel()
            activityLoadJob =
                viewModelScope.launch {
                    when (val result = getMyActivity(requestedMonth.year, requestedMonth.monthValue)) {
                        is AppResult.Success -> {
                            _uiState.value =
                                result.value.toActivityUiState(LocalDate.now()) ?: ActivityUiState.LoadFailed
                        }

                        is AppResult.Failure -> {
                            _uiState.value = ActivityUiState.LoadFailed
                        }
                    }
                }
        }
    }
