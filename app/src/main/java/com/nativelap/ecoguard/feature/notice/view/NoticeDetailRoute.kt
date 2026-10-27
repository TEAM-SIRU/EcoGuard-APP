package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailViewModel
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent

@Composable
fun NoticeDetailRoute(
    noticeId: Long,
    onEvent: (NoticeScreenEvent) -> Unit,
    viewModel: NoticeDetailViewModel? = null,
) {
    val noticeDetailViewModel =
        viewModel ?: rememberActivityViewModel<NoticeDetailViewModel>("notice-detail:$noticeId")
    val uiState by noticeDetailViewModel.uiState.collectAsState()
    LaunchedEffect(noticeId) {
        noticeDetailViewModel.load(noticeId)
    }
    NoticeDetailScreen(uiState) { event ->
        when (event) {
            NoticeScreenEvent.RetryClick -> noticeDetailViewModel.load(noticeId)
            NoticeScreenEvent.ReturnToListClick, NoticeScreenEvent.BackClick -> onEvent(event)
            is NoticeScreenEvent.NoticeClick -> onEvent(event)
        }
    }
}
