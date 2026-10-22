package com.nativelap.ecoguard.feature.notice.viewmodel

sealed interface NoticeUiState {
    data object Loading : NoticeUiState

    data object Empty : NoticeUiState

    data object Failed : NoticeUiState

    data class Content(
        val notices: List<NoticeUiModel>,
    ) : NoticeUiState
}
