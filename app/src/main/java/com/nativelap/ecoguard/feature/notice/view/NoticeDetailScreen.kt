package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiModel
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.SkeletonPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun NoticeDetailScreen(
    uiState: NoticeDetailUiState,
    onEvent: (NoticeScreenEvent) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EcoBackTopBar(
                onBackClick = { onEvent(NoticeScreenEvent.BackClick) },
                title = stringResource(R.string.notice_title),
            )
        },
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
            ) {
                when (uiState) {
                    is NoticeDetailUiState.Content -> {
                        NoticeDetailBody(uiState.notice)
                    }

                    NoticeDetailUiState.Loading -> {
                        repeat(7) { SkeletonPlaceholder(Modifier.fillMaxWidth().height(AppSpacing.xl)) }
                    }

                    NoticeDetailUiState.Failed, NoticeDetailUiState.Missing -> {
                        InlineEmptyState(
                            R.drawable.ic_map_30,
                            stringResource(
                                if (uiState ==
                                    NoticeDetailUiState.Missing
                                ) {
                                    R.string.notice_missing
                                } else {
                                    R.string.notice_detail_failed
                                },
                            ),
                            stringResource(
                                if (uiState == NoticeDetailUiState.Missing) {
                                    R.string.notice_missing_description
                                } else {
                                    R.string.common_try_again_later
                                },
                            ),
                            actionText =
                                stringResource(
                                    if (uiState ==
                                        NoticeDetailUiState.Missing
                                    ) {
                                        R.string.notice_return_to_list
                                    } else {
                                        R.string.action_retry
                                    },
                                ),
                            onActionClick = {
                                onEvent(
                                    if (uiState ==
                                        NoticeDetailUiState.Missing
                                    ) {
                                        NoticeScreenEvent.ReturnToListClick
                                    } else {
                                        NoticeScreenEvent.RetryClick
                                    },
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NoticeDetailPreview() {
    EcoGuardTheme {
        NoticeDetailScreen(
            NoticeDetailUiState.Content(NoticeUiModel(1, "공지사항 예시", "2026.10.25", "공지 내용")),
            {
            },
        )
    }
}
