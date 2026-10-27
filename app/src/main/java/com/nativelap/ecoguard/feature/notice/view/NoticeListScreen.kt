package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiModel
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiState
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.SkeletonPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun NoticeListScreen(
    uiState: NoticeUiState,
    onEvent: (NoticeScreenEvent) -> Unit,
) {
    if (uiState == NoticeUiState.Failed) {
        NoticeLoadFailedScreen({ onEvent(NoticeScreenEvent.BackClick) }, { onEvent(NoticeScreenEvent.RetryClick) })
        return
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { EcoBackTopBar(onBackClick = { onEvent(NoticeScreenEvent.BackClick) }) },
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(Modifier.widthIn(max = AppComponentSize.contentMaxWidth).fillMaxSize()) {
                PageTitle(
                    stringResource(R.string.notice_title),
                    Modifier.padding(
                        start = AppSpacing.xl,
                        end = AppSpacing.xl,
                        top = AppSpacing.xs,
                        bottom = AppSpacing.lg,
                    ),
                )
                LazyColumn(
                    Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(start = AppSpacing.xl, end = AppSpacing.xl, bottom = AppSpacing.xl),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                ) {
                    when (uiState) {
                        is NoticeUiState.Content -> {
                            item {
                                Text(
                                    stringResource(R.string.format_total_count, uiState.notices.size),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.extraColors.captionTextColor,
                                )
                            }
                            items(uiState.notices, key = { it.noticeId }) { notice ->
                                NoticeCard(notice, { onEvent(NoticeScreenEvent.NoticeClick(notice.noticeId)) })
                            }
                        }

                        NoticeUiState.Loading -> {
                            items(3) { SkeletonPlaceholder(Modifier.fillMaxWidth().height(AppComponentSize.logo)) }
                        }

                        NoticeUiState.Empty -> {
                            item {
                                Text(
                                    stringResource(R.string.format_total_count, 0),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            item {
                                InlineEmptyState(
                                    R.drawable.ic_map_30,
                                    stringResource(R.string.notice_empty),
                                    stringResource(R.string.notice_empty_description),
                                    Modifier.padding(vertical = AppSpacing.xl),
                                )
                            }
                        }

                        NoticeUiState.Failed -> {
                            Unit
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NoticeListPreview() {
    EcoGuardTheme {
        NoticeListScreen(
            NoticeUiState.Content(
                listOf(NoticeUiModel(1, "공지사항 예시", "2026.10.25", "공지 미리보기")),
            ),
            {},
        )
    }
}
