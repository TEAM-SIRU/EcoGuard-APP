package com.nativelap.ecoguard.feature.notice.viewmodel

sealed interface NoticeDetailUiState {
    data object Loading : NoticeDetailUiState

    data object Failed : NoticeDetailUiState

    data object Missing : NoticeDetailUiState

    data class Content(
        val notice: NoticeUiModel,
    ) : NoticeDetailUiState
}
