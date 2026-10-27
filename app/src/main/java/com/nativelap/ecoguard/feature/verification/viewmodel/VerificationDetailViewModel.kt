package com.nativelap.ecoguard.feature.verification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatus
import com.nativelap.ecoguard.feature.verification.module.domain.usecase.GetMyVerificationsUseCase
import com.nativelap.ecoguard.ui.component.StatusChipType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class VerificationDetailViewModel
    @Inject
    constructor(
        private val getMyVerifications: GetMyVerificationsUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<VerificationDetailUiState>(VerificationDetailUiState.Loading)
        val uiState = mutableUiState.asStateFlow()
        private var loadJob: Job? = null

        fun load(verificationId: Long) {
            loadJob?.cancel()
            mutableUiState.value = VerificationDetailUiState.Loading
            loadJob =
                viewModelScope.launch {
                    mutableUiState.value =
                        when (val result = getMyVerifications()) {
                            is AppResult.Success -> {
                                result.value
                                    .firstOrNull { it.verificationId == verificationId }
                                    ?.toDetailUiState()
                                    ?: VerificationDetailUiState.Failed
                            }

                            is AppResult.Failure -> {
                                VerificationDetailUiState.Failed
                            }
                        }
                }
        }
    }

private fun MyVerification.toDetailUiState(): VerificationDetailUiState {
    val recordDate = date.toDisplayDate() ?: return VerificationDetailUiState.Failed
    val time =
        submittedAt?.substringAfter('T')?.take(5)?.takeIf(String::isNotBlank) ?: return VerificationDetailUiState.Failed
    val area = areaName?.takeIf(String::isNotBlank) ?: return VerificationDetailUiState.Failed
    val state = reviewStatus?.value ?: return VerificationDetailUiState.Failed
    val (chip, descriptionRes) =
        when (state) {
            VerificationStatus.PROCESSING, VerificationStatus.MANUAL_REVIEW -> {
                StatusChipType.REVIEWING to
                    R.string.home_ai_reviewing
            }

            VerificationStatus.APPROVED -> {
                StatusChipType.APPROVED to R.string.verification_status_approved
            }

            VerificationStatus.REJECTED -> {
                StatusChipType.REJECTED to R.string.common_verification_rejected
            }

            VerificationStatus.UNKNOWN -> {
                return VerificationDetailUiState.Failed
            }
        }
    return VerificationDetailUiState.Content(
        verificationDate = recordDate,
        statusDescriptionRes = descriptionRes,
        rejectionReason = failReasons?.firstOrNull()?.takeIf(String::isNotBlank),
        status = chip,
        areaName = area,
        submittedDateTime = "$recordDate $time",
    )
}

private fun String?.toDisplayDate(): String? =
    runCatching { LocalDate.parse(this).format(DateTimeFormatter.ofPattern("M월 d일")) }
        .getOrNull()
