package com.nativelap.ecoguard.feature.recruitment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.ApplyForRecruitmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel
    @Inject
    constructor(
        private val applyForRecruitment: ApplyForRecruitmentUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ApplicationUiState?>(null)
        val uiState: StateFlow<ApplicationUiState?> = _uiState.asStateFlow()

        private val _submissionResult = MutableStateFlow<ApplicationResultUiState.Completed?>(null)
        val submissionResult: StateFlow<ApplicationResultUiState.Completed?> = _submissionResult.asStateFlow()

        private var recruitmentId: Long? = null
        private var isSubmitting = false

        fun initialize(
            recruitmentId: Long,
            initialUiState: ApplicationUiState,
        ) {
            if (this.recruitmentId == recruitmentId && _uiState.value != null) return
            this.recruitmentId = recruitmentId
            _submissionResult.value = null
            _uiState.value = initialUiState.copy(motivation = "", isApplying = false, isSubmissionFailed = false)
        }

        fun consumeSubmissionResult() {
            _submissionResult.value = null
        }

        fun updateMotivation(motivation: String) {
            _uiState.value =
                _uiState.value?.copy(
                    motivation =
                        com.nativelap.ecoguard.ui.component.TextInputLength.limit(
                            motivation,
                            ApplicationUiState.MAX_MOTIVATION_LENGTH,
                        ),
                    isSubmissionFailed = false,
                )
        }

        fun submitApplication() {
            val currentState = _uiState.value ?: return
            val currentRecruitmentId = recruitmentId ?: return
            if (!currentState.canApply || isSubmitting) return

            isSubmitting = true
            viewModelScope.launch {
                _uiState.value = currentState.copy(isApplying = true, isSubmissionFailed = false)
                when (
                    val result =
                        applyForRecruitment(
                            ApplyCommand(
                                recruitmentId = currentRecruitmentId,
                                motivation = currentState.motivation.trim(),
                            ),
                        )
                ) {
                    is AppResult.Success -> {
                        val completedState = result.value.toCompletedUiStateOrNull()
                        isSubmitting = false
                        if (completedState == null) {
                            _uiState.value = currentState.copy(isApplying = false, isSubmissionFailed = true)
                        } else {
                            _uiState.value = currentState.copy(isApplying = false)
                            _submissionResult.value = completedState
                        }
                    }

                    is AppResult.Failure -> {
                        isSubmitting = false
                        _uiState.value = currentState.copy(isApplying = false, isSubmissionFailed = true)
                    }
                }
            }
        }
    }

private fun ApplyResult.toCompletedUiStateOrNull(): ApplicationResultUiState.Completed? {
    val applicationOrder = order?.takeIf { it in 1L..Int.MAX_VALUE.toLong() }?.toInt() ?: return null
    val dateTime = appliedAt ?: return null
    val formattedDateTime =
        runCatching {
            LocalDateTime.parse(dateTime).format(DateTimeFormatter.ofPattern("M월 d일 HH:mm"))
        }.getOrElse { dateTime }

    return ApplicationResultUiState.Completed(
        applicationOrder = applicationOrder,
        appliedDateTime =
        formattedDateTime,
    )
}
