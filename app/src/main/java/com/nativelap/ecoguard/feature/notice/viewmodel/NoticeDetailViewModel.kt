package com.nativelap.ecoguard.feature.notice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.feature.notice.module.domain.usecase.GetNoticeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeDetailViewModel
    @Inject
    constructor(
        private val getNoticeDetail: GetNoticeDetailUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<NoticeDetailUiState>(NoticeDetailUiState.Loading)
        val uiState = mutableUiState.asStateFlow()
        private var loadJob: Job? = null

        fun load(noticeId: Long) {
            loadJob?.cancel()
            mutableUiState.value = NoticeDetailUiState.Loading
            loadJob =
                viewModelScope.launch {
                    mutableUiState.value = getNoticeDetail(noticeId).toNoticeDetailUiState()
                }
        }
    }
