package com.nativelap.ecoguard.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.activity.module.domain.usecase.GetMyActivityUseCase
import com.nativelap.ecoguard.feature.activity.module.domain.usecase.GetMyWeeklyActivityUseCase
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase.GetMyCleaningAssignmentUseCase
import com.nativelap.ecoguard.feature.notice.module.domain.usecase.GetNoticesUseCase
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.GetCurrentRecruitmentUseCase
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.GetMyApplicationStatusUseCase
import com.nativelap.ecoguard.feature.verification.module.domain.usecase.GetMyVerificationsUseCase
import com.nativelap.ecoguard.feature.verification.module.domain.usecase.GetTodayVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getMyCleaningAssignment: GetMyCleaningAssignmentUseCase,
        private val getCurrentRecruitment: GetCurrentRecruitmentUseCase,
        private val getMyApplicationStatus: GetMyApplicationStatusUseCase,
        private val getNotices: GetNoticesUseCase,
        private val getTodayVerification: GetTodayVerificationUseCase,
        private val getMyVerifications: GetMyVerificationsUseCase,
        private val getMyWeeklyActivity: GetMyWeeklyActivityUseCase,
        private val getMyActivity: GetMyActivityUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
        val uiState = _uiState.asStateFlow()
        private var homeLoadJob: Job? = null

        init {
            loadHome()
        }

        fun loadHome() {
            homeLoadJob?.cancel()
            _uiState.value = HomeUiState.Loading
            homeLoadJob =
                viewModelScope.launch {
                    _uiState.value = loadHomeState()
                }
        }

        fun dismissNotice() {
            val currentState = _uiState.value as? HomeUiState.Content ?: return
            _uiState.value = currentState.copy(notice = null)
        }

        private suspend fun loadHomeState(): HomeUiState =
            coroutineScope {
                val assignmentRequest = async { getMyCleaningAssignment() }
                val noticeRequest = async { getNotices() }

                val assignmentResult = assignmentRequest.await()
                val noticeResult = noticeRequest.await()

                val notice =
                    when (noticeResult) {
                        is AppResult.Success -> noticeResult.value.toHomeNoticeUiModel()
                        is AppResult.Failure -> null
                    }
                when (assignmentResult) {
                    is AppResult.Success -> {
                        val today = LocalDate.now()
                        val todayVerificationRequest = async { getTodayVerification() }
                        val myVerificationsRequest = async { getMyVerifications() }
                        val weeklyActivityRequest = async { getMyWeeklyActivity() }
                        val monthlyActivityRequest = async { getMyActivity(today.year, today.monthValue) }
                        val todayVerificationResult = todayVerificationRequest.await()
                        val myVerificationsResult = myVerificationsRequest.await()
                        val weeklyActivityResult = weeklyActivityRequest.await()
                        val monthlyActivityResult = monthlyActivityRequest.await()

                        val todayVerification =
                            todayVerificationResult.valueOrNull()
                                ?: return@coroutineScope HomeUiState.LoadFailed
                        val myVerifications =
                            myVerificationsResult.valueOrNull()
                                ?: return@coroutineScope HomeUiState.LoadFailed
                        val weeklyActivity =
                            weeklyActivityResult.valueOrNull()
                                ?: return@coroutineScope HomeUiState.LoadFailed
                        val monthlyActivity =
                            monthlyActivityResult.valueOrNull()
                                ?: return@coroutineScope HomeUiState.LoadFailed
                        val section =
                            assignmentResult.value.toHomeCleaningSection(
                                todayVerification = todayVerification,
                                myVerifications = myVerifications,
                                weeklyActivity = weeklyActivity,
                                monthlyActivity = monthlyActivity,
                                today = today,
                            ) ?: return@coroutineScope HomeUiState.LoadFailed

                        HomeUiState.Content(notice = notice, section = section)
                    }

                    is AppResult.Failure -> {
                        if (!assignmentResult.error.isNotFound()) {
                            return@coroutineScope HomeUiState.LoadFailed
                        }
                        val recruitmentRequest = async { getCurrentRecruitment() }
                        val applicationRequest = async { getMyApplicationStatus() }
                        val recruitmentResult = recruitmentRequest.await()
                        val applicationResult = applicationRequest.await()
                        val applicationStatus =
                            when (applicationResult) {
                                is AppResult.Success -> {
                                    applicationResult.value
                                }

                                is AppResult.Failure -> {
                                    if (!applicationResult.error.isNotFound()) {
                                        return@coroutineScope HomeUiState.LoadFailed
                                    }
                                    null
                                }
                            }
                        when (recruitmentResult) {
                            is AppResult.Success -> {
                                val section =
                                    recruitmentResult.value.toHomeSection(applicationStatus)
                                        ?: return@coroutineScope HomeUiState.LoadFailed
                                HomeUiState.Content(notice = notice, section = section)
                            }

                            is AppResult.Failure -> {
                                if (recruitmentResult.error.isNotFound() && applicationStatus == null) {
                                    HomeUiState.Content(
                                        notice = notice,
                                        section = HomeSectionUiModel.NoRecruitment,
                                    )
                                } else if (recruitmentResult.error.isNotFound() &&
                                    applicationStatus?.waitingForAssignment == true
                                ) {
                                    HomeUiState.Content(
                                        notice = notice,
                                        section = HomeSectionUiModel.WaitingAssignment,
                                    )
                                } else {
                                    HomeUiState.LoadFailed
                                }
                            }
                        }
                    }
                }
            }
    }

private fun AppError.isNotFound(): Boolean = this is AppError.Http && statusCode == HTTP_NOT_FOUND

private fun <T> AppResult<T>.valueOrNull(): T? =
    when (this) {
        is AppResult.Success -> value
        is AppResult.Failure -> null
    }

private const val HTTP_NOT_FOUND = 404
