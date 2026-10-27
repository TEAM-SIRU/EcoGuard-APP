package com.nativelap.ecoguard.feature.notice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.feature.notice.module.domain.usecase.GetNoticesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel
    @Inject
    constructor(
        private val getNotices: GetNoticesUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<NoticeUiState>(NoticeUiState.Loading)
        val uiState = mutableUiState.asStateFlow()

        init {
            loadNotices()
        }

        fun retry() {
            loadNotices()
        }

        private fun loadNotices() {
            mutableUiState.value = NoticeUiState.Loading
            viewModelScope.launch {
                mutableUiState.value = getNotices().toNoticeUiState()
            }
        }
    }
