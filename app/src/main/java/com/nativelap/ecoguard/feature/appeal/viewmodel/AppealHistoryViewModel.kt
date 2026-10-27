package com.nativelap.ecoguard.feature.appeal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealStatus
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal
import com.nativelap.ecoguard.feature.appeal.module.domain.usecase.GetMyAppealsUseCase
import com.nativelap.ecoguard.ui.component.StatusChipType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AppealHistoryViewModel
    @Inject
    constructor(
        private val getMyAppeals: GetMyAppealsUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<AppealHistoryUiState>(AppealHistoryUiState.Loading)
        val uiState: StateFlow<AppealHistoryUiState> = _uiState.asStateFlow()

        fun loadHistory() {
            _uiState.value = AppealHistoryUiState.Loading
            viewModelScope.launch {
                when (val result = getMyAppeals()) {
                    is AppResult.Success -> {
                        val items = result.value.mapNotNull(MyAppeal::toUiModel)
                        _uiState.value =
                            when {
                                items.size != result.value.size -> AppealHistoryUiState.LoadFailed
                                items.isEmpty() -> AppealHistoryUiState.Empty
                                else -> AppealHistoryUiState.Content(items)
                            }
                    }

                    is AppResult.Failure -> {
                        _uiState.value = AppealHistoryUiState.LoadFailed
                    }
                }
            }
        }
    }

private fun MyAppeal.toUiModel(): AppealHistoryItemUiModel? {
    val validAppealId = appealId?.takeIf { it > 0L } ?: return null
    val formattedVerificationDate = verificationDate.toDisplayDate() ?: return null
    val validRound = round?.takeIf { it > 0 } ?: return null
    val formattedCreatedAt = createdAt.toDisplayDateTime() ?: return null
    val chip =
        when (status) {
            AppealStatus.Pending -> StatusChipType.REVIEWING
            AppealStatus.Approved -> StatusChipType.APPROVED
            AppealStatus.Rejected -> StatusChipType.REJECTED
            is AppealStatus.Unknown, null -> return null
        }
    return AppealHistoryItemUiModel(
        appealId = validAppealId,
        verificationDate = formattedVerificationDate,
        attemptNumber = validRound,
        status = chip,
        sentDateTime = formattedCreatedAt,
        earnedMinutes = awardedMinutes,
    )
}

private fun String?.toDisplayDate(): String? =
    runCatching { LocalDate.parse(this).format(DateTimeFormatter.ofPattern("M월 d일")) }.getOrNull()

private fun String?.toDisplayDateTime(): String? =
    runCatching { OffsetDateTime.parse(this).toLocalDateTime().format(DateTimeFormatter.ofPattern("M월 d일 HH:mm")) }
        .recoverCatching { LocalDateTime.parse(this).format(DateTimeFormatter.ofPattern("M월 d일 HH:mm")) }
        .getOrNull()
