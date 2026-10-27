package com.nativelap.ecoguard.feature.recruitment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.GetCurrentRecruitmentUseCase
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.GetMyApplicationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RecruitmentViewModel
    @Inject
    constructor(
        private val getCurrentRecruitment: GetCurrentRecruitmentUseCase,
        private val getMyApplicationStatus: GetMyApplicationStatusUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<RecruitmentUiState>(RecruitmentUiState.Loading)
        val uiState: StateFlow<RecruitmentUiState> = _uiState.asStateFlow()
        private var loadJob: Job? = null

        fun loadRecruitment() {
            loadJob?.cancel()
            _uiState.value = RecruitmentUiState.Loading
            loadJob =
                viewModelScope.launch {
                    when (val result = getCurrentRecruitment()) {
                        is AppResult.Success -> {
                            val currentRecruitment = result.value
                            val myApplication =
                                if (currentRecruitment.alreadyApplied == true) {
                                    when (val statusResult = getMyApplicationStatus()) {
                                        is AppResult.Success -> {
                                            statusResult.value
                                        }

                                        is AppResult.Failure -> {
                                            _uiState.value = RecruitmentUiState.LoadFailed
                                            return@launch
                                        }
                                    }
                                } else {
                                    null
                                }
                            _uiState.value =
                                currentRecruitment.toUiState(myApplication) ?: RecruitmentUiState.LoadFailed
                        }

                        is AppResult.Failure -> {
                            _uiState.value = RecruitmentUiState.LoadFailed
                        }
                    }
                }
        }
    }

private fun CurrentRecruitment.toUiState(myApplication: MyApplicationStatus?): RecruitmentUiState.Content? {
    val currentRecruitmentId = recruitmentId ?: return null
    val currentSemester = semester?.takeIf(String::isNotBlank) ?: return null
    val currentGrade = grade ?: return null
    val currentClassNo = classNo ?: return null
    val currentMaxCount = maxCount?.takeIf { it > 0 } ?: return null
    val currentApplicantCount = currentApplicants?.takeIf { it >= 0 } ?: return null
    val currentPeriod = period ?: return null
    val currentActivityTime = activityTime ?: return null
    if (currentPeriod.start.isNullOrBlank() || currentPeriod.end.isNullOrBlank()) return null
    if (currentActivityTime.start.isNullOrBlank() || currentActivityTime.end.isNullOrBlank()) return null
    if (alreadyApplied == true && (myApplication?.order == null || myApplication.appliedAt == null)) return null

    val spots = (currentMaxCount - currentApplicantCount.toInt()).coerceAtLeast(0)
    val applicationStatus =
        when {
            alreadyApplied == true && myApplication != null -> {
                RecruitmentApplicationStatus.AlreadyApplied(
                    appliedDateTime = myApplication.appliedAt.toDisplayDateTime(),
                    applicationOrder = myApplication.order?.toInt() ?: 0,
                )
            }

            periodStatus.value == RecruitmentPeriodStatus.OPEN && full != true -> {
                RecruitmentApplicationStatus.Open(remainingSpots = spots)
            }

            else -> {
                RecruitmentApplicationStatus.Closed
            }
        }

    return RecruitmentUiState.Content(
        recruitmentId = recruitmentId ?: 0L,
        semesterName = currentSemester,
        maxApplicantsPerClass = currentMaxCount,
        periodStartDate = currentPeriod.start.toDisplayDate(),
        periodEndDate = currentPeriod.end.toDisplayDate(),
        activityStartTime = currentActivityTime.start.toDisplayTime(),
        activityEndTime = currentActivityTime.end.toDisplayTime(),
        grade = currentGrade,
        classNumber = currentClassNo,
        appliedCount = currentApplicantCount.coerceAtMost(Int.MAX_VALUE.toLong()).toInt(),
        volunteerMinutesPerVerification = 10,
        applicationStatus = applicationStatus,
    )
}

private fun String?.toDisplayDate(): String =
    runCatching {
        LocalDateTime.parse(this).format(DateTimeFormatter.ofPattern("M월 d일"))
    }.getOrElse { this.orEmpty() }

private fun String?.toDisplayDateTime(): String =
    runCatching {
        LocalDateTime.parse(this).format(DateTimeFormatter.ofPattern("M월 d일 HH:mm"))
    }.getOrElse { this.orEmpty() }

private fun String?.toDisplayTime(): String =
    runCatching {
        DateTimeFormatter.ofPattern("HH:mm").format(java.time.LocalTime.parse(this))
    }.getOrElse { this.orEmpty() }
