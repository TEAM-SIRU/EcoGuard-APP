package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeListViewModel
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent

@Composable
fun NoticeListRoute(
    onEvent: (NoticeScreenEvent) -> Unit,
    viewModel: NoticeListViewModel? = null,
) {
    val noticeListViewModel =
        viewModel ?: rememberActivityViewModel<NoticeListViewModel>()
    val uiState by noticeListViewModel.uiState.collectAsState()
    NoticeListScreen(uiState) { event ->
        when (event) {
            NoticeScreenEvent.RetryClick -> noticeListViewModel.retry()
            else -> onEvent(event)
        }
    }
}
