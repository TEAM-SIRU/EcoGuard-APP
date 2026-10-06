package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentUiState
import com.nativelap.ecoguard.ui.component.LoadingSkeletonLayout

// 서버 연동 전까지는 전달받은 고정 상태를 표시한다. ViewModel 연결은 모집 기능 작업에서 추가한다.
@Composable
fun RecruitmentRoute(
    uiState: RecruitmentUiState,
    onEvent: (RecruitmentScreenEvent) -> Unit = {},
) {
    when (uiState) {
        RecruitmentUiState.Loading -> LoadingSkeletonLayout(
            title = stringResource(R.string.recruitment_notice_title),
        )
        RecruitmentUiState.LoadFailed -> RecruitmentLoadFailedScreen(onEvent = onEvent)
        is RecruitmentUiState.Content -> RecruitmentScreen(
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}
