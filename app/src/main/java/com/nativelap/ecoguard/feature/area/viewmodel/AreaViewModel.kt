package com.nativelap.ecoguard.feature.area.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase.GetMyCleaningAssignmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AreaViewModel
    @Inject
    constructor(
        private val getMyCleaningAssignment: GetMyCleaningAssignmentUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<AreaUiState>(AreaUiState.Loading)
        val uiState = _uiState.asStateFlow()

        private var selectedFloor = DEFAULT_SELECTED_FLOOR
        private var assignmentLoadJob: Job? = null

        init {
            loadAssignment()
        }

        fun loadAssignment() {
            _uiState.value = AreaUiState.Loading
            assignmentLoadJob?.cancel()
            assignmentLoadJob =
                viewModelScope.launch {
                    when (val result = getMyCleaningAssignment()) {
                        is AppResult.Success -> {
                            val assignment = result.value
                            _uiState.value =
                                AreaUiState.Content(
                                    floors = AVAILABLE_FLOORS,
                                    selectedFloor = selectedFloor,
                                    areaName = assignment.areaName,
                                    areaDescription = assignment.description,
                                    cleaningTime = assignment.cleanTime,
                                    memberNames = assignment.members.map { it.name },
                                )
                        }

                        is AppResult.Failure -> {
                            _uiState.value =
                                if (result.error == AppError.Http(NO_ASSIGNMENT_STATUS_CODE)) {
                                    AreaUiState.NotAssigned
                                } else {
                                    AreaUiState.LoadFailed
                                }
                        }
                    }
                }
        }

        fun selectFloor(floor: Int) {
            if (floor !in AVAILABLE_FLOORS) return
            selectedFloor = floor
            val currentState = _uiState.value
            if (currentState is AreaUiState.Content) {
                _uiState.value = currentState.copy(selectedFloor = floor)
            }
        }

        private companion object {
            const val DEFAULT_SELECTED_FLOOR = 1
            const val NO_ASSIGNMENT_STATUS_CODE = 404
            val AVAILABLE_FLOORS = listOf(1, 2, 3, 4)
        }
    }
