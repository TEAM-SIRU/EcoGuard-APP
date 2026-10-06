package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.CleaningRecordUiModel
import com.nativelap.ecoguard.ui.component.CleaningRecordCard
import com.nativelap.ecoguard.ui.component.SectionHeader
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val recentRecordSpacing = 10.dp

/** 최근 청소 기록 섹션 제목과 기록 카드 목록. */
@Composable
fun HomeRecentRecordsContent(
    recentRecords: List<CleaningRecordUiModel>,
    onAllRecordsClick: () -> Unit,
    onRecordClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(recentRecordSpacing),
    ) {
        SectionHeader(
            title = stringResource(R.string.home_recent_cleaning_records),
            actionText = stringResource(R.string.action_view_all),
            onActionClick = onAllRecordsClick,
        )

        recentRecords.forEach { cleaningRecord ->
            CleaningRecordCard(
                submittedDateTime = cleaningRecord.submittedDateTime,
                areaName = cleaningRecord.areaName,
                status = cleaningRecord.status,
                earnedMinutes = cleaningRecord.earnedMinutes,
                onClick = { onRecordClick(cleaningRecord.recordId) },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390)
@Composable
private fun HomeRecentRecordsContentPreview() {
    EcoGuardTheme {
        HomeRecentRecordsContent(
            recentRecords = HomePreviewFixtures.recentRecords,
            onAllRecordsClick = {},
            onRecordClick = {},
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
