package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 10 공지 조회 실패. 공지 목록·상세 화면은 Figma에 없어 이 상태만 구현한다. */
@Composable
fun NoticeLoadFailedScreen(
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxSize(),
            ) {
                PageTitle(
                    title = stringResource(R.string.action_notice),
                    modifier = Modifier.padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal,
                        top = AppSpacing.xs,
                        bottom = AppSpacing.lg,
                    ),
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = AppSpacing.screenHorizontal,
                            vertical = AppSpacing.xl,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    InlineEmptyState(
                        iconRes = R.drawable.ic_map_30,
                        title = stringResource(R.string.notice_load_failed),
                        description = stringResource(R.string.common_try_again_later),
                        actionText = stringResource(R.string.action_retry),
                        onActionClick = onRetryClick,
                    )
                }
            }
        }
    }
}

@Preview(name = "Notice · load failed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NoticeLoadFailedScreenPreview() {
    EcoGuardTheme {
        NoticeLoadFailedScreen(
            onBackClick = {},
            onRetryClick = {},
        )
    }
}
