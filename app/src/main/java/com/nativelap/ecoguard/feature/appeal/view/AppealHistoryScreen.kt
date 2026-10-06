package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryItemUiModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.StatusChip
import com.nativelap.ecoguard.ui.component.StatusChipType
import com.nativelap.ecoguard.ui.component.TwoLineTextRow
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 09-3 이의신청 내역. 인증별 차수, 보낸 시각, 결과를 목록으로 보여준다. */
@Composable
fun AppealHistoryScreen(
    appealHistoryItems: List<AppealHistoryItemUiModel>,
    onEvent: (AppealScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(onBackClick = { onEvent(AppealScreenEvent.BackClick) })
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth(),
            ) {
                item {
                    PageTitle(
                        title = stringResource(R.string.profile_appeal_history),
                        modifier = Modifier.padding(
                            start = AppSpacing.screenHorizontal,
                            end = AppSpacing.screenHorizontal,
                            top = AppSpacing.xs,
                            bottom = AppSpacing.lg,
                        ),
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.format_total_count, appealHistoryItems.size),
                        modifier = Modifier.padding(
                            start = AppSpacing.screenHorizontal,
                            end = AppSpacing.screenHorizontal,
                            top = AppSpacing.md,
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.extraColors.captionTextColor,
                    )
                }

                items(
                    items = appealHistoryItems,
                    key = { appealHistoryItem -> appealHistoryItem.appealId },
                ) { appealHistoryItem ->
                    AppealHistoryRow(
                        appealHistoryItem = appealHistoryItem,
                        onClick = { onEvent(AppealScreenEvent.HistoryItemClick(appealHistoryItem.appealId)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun AppealHistoryRow(
    appealHistoryItem: AppealHistoryItemUiModel,
    onClick: () -> Unit,
) {
    val separator = stringResource(R.string.format_list_separator)
    val subtitleParts = buildList {
        add(stringResource(R.string.appeal_attempt, appealHistoryItem.attemptNumber))
        add(stringResource(R.string.appeal_sent_suffix, appealHistoryItem.sentDateTime))

        if (appealHistoryItem.earnedMinutes != null) {
            add(stringResource(R.string.format_bonus_minutes, appealHistoryItem.earnedMinutes))
        }
    }
    val chipLabel = if (appealHistoryItem.status == StatusChipType.REVIEWING) {
        stringResource(R.string.appeal_review_pending)
    } else {
        stringResource(appealHistoryItem.status.labelRes)
    }

    TwoLineTextRow(
        title = stringResource(R.string.verification_detail_title, appealHistoryItem.verificationDate),
        subtitle = subtitleParts.joinToString(separator = separator),
        modifier = Modifier.clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        StatusChip(
            type = appealHistoryItem.status,
            label = chipLabel,
        )
    }
}

@Preview(name = "Appeal history", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppealHistoryScreenPreview() {
    EcoGuardTheme {
        AppealHistoryScreen(
            appealHistoryItems = AppealPreviewFixtures.historyItems,
            onEvent = {},
        )
    }
}
